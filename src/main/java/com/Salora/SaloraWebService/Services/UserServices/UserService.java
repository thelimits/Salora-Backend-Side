package com.Salora.SaloraWebService.Services.UserServices;

import com.Salora.SaloraWebService.Config.JwtAuth.JwtService;
import com.Salora.SaloraWebService.DTO.ProductDTO.ProductDTO;
import com.Salora.SaloraWebService.DTO.RequestDTO.RequestUpdateUserAdditionalDetails;
import com.Salora.SaloraWebService.DTO.ResponseDTO.GetUserAdditionalDetails;
import com.Salora.SaloraWebService.DTO.ResponseDTO.GetUserProfileDTO;
import com.Salora.SaloraWebService.Exception.CustomerNotFound;
import com.Salora.SaloraWebService.Model.Product;
import com.Salora.SaloraWebService.Model.User;
import com.Salora.SaloraWebService.Model.UserAdditionalDetails;
import com.Salora.SaloraWebService.Model.Wishlist;
import com.Salora.SaloraWebService.Repository.ProductRepository;
import com.Salora.SaloraWebService.Repository.UserRepository;
import com.Salora.SaloraWebService.Repository.WishlistRepository;
import com.Salora.SaloraWebService.Utils.ProductMapper;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService implements IUserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private WishlistRepository wishlistRepository;

    @Autowired
    private ProductMapper mapper;

    @Autowired
    private JwtService jwtService;

    @Override
    @Async
    public CompletableFuture<GetUserProfileDTO> getUserProfile(@NonNull HttpServletRequest request,
                                            @NonNull HttpServletResponse response,
                                            @NonNull FilterChain filterChain) {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;

        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            throw new JwtException("False Token");
        }

        jwt = authHeader.substring(7);

        String email = jwtService.extractUsername(jwt);

        Optional<User> user = userRepository.findByEmail(email);

        if(user.isEmpty()){
            throw new CustomerNotFound("Account Not Found");
        }

        return CompletableFuture.completedFuture(mapper.mapObject(user.get(), GetUserProfileDTO.class)) ;
    }

    @Override
    public ResponseEntity<?> addWishlist(@NonNull HttpServletRequest request,
                                          @NonNull HttpServletResponse response,
                                          @NonNull FilterChain filterChain,
                                          String id) {
        Wishlist wishlist = new Wishlist();
        Map<String, String> body = new HashMap<>();

        final String authHeader = request.getHeader("Authorization");
        final String jwt;

        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            throw new JwtException("False Token");
        }

        jwt = authHeader.substring(7);

        String email = jwtService.extractUsername(jwt);

        ExecutorService executorService = Executors.newFixedThreadPool(4);

        CompletableFuture<Optional<User>> userFuture = CompletableFuture.supplyAsync(() -> userRepository.findByEmail(email), executorService);
        CompletableFuture<Optional<Product>> productFuture = CompletableFuture.supplyAsync(() -> productRepository.findProductById(id), executorService);

        CompletableFuture<Void> completableFuture = CompletableFuture.allOf(userFuture, productFuture);

        try {
            completableFuture.get(5, TimeUnit.SECONDS);
        } catch (ExecutionException | InterruptedException | TimeoutException e) {
            e.printStackTrace();
        }finally {
            executorService.shutdown();
        }

        Optional<User> user = userFuture.join();
        Optional<Product> product = productFuture.join();

        if(user.isEmpty() || product.isEmpty()){
            throw new CustomerNotFound("Account or Product Not Found");
        }

        wishlist.setProduct(product.get());
        wishlist.setUser(user.get());

        wishlistRepository.save(wishlist);

        body.put("response", "Success added wishlist");

        return ResponseEntity.status(HttpStatus.OK).body(body);
    }

    @Override
    public ResponseEntity<?> deleteWhislist(@NonNull HttpServletRequest request,
                                            @NonNull HttpServletResponse response,
                                            @NonNull FilterChain filterChain,
                                            String id) {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;

        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            throw new JwtException("False Token");
        }

        jwt = authHeader.substring(7);

        String email = jwtService.extractUsername(jwt);

        Optional<User> user = userRepository.findByEmail(email);

        if(user.isEmpty()){
            throw new CustomerNotFound("Account Not Found");
        }

        wishlistRepository.deleteWishlist(user.get().getId(), id);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Override
    public ResponseEntity<?> checkWishlists(@NonNull HttpServletRequest request,
                                            @NonNull HttpServletResponse response,
                                            @NonNull FilterChain filterChain) {
        Map<String, List<String>> mapResponse = new HashMap<>();
        final String authHeader = request.getHeader("Authorization");
        final String jwt;

        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            throw new JwtException("False Token");
        }

        jwt = authHeader.substring(7);

        String email = jwtService.extractUsername(jwt);

        Optional<User> user = userRepository.findByEmail(email);

        if(user.isEmpty()){
            throw new CustomerNotFound("Account Not Found");
        }

        List<String> products = productRepository.findWishlistProductByUserId(user.get().getId())
                .stream()
                .map(Product::getId)
                .toList();

        mapResponse.put("wishlists", products);
        return ResponseEntity.status(HttpStatus.OK).body(mapResponse);
    }

    @Override
    public List<ProductDTO> getWishlist(@NonNull HttpServletRequest request,
                                        @NonNull HttpServletResponse response,
                                        @NonNull FilterChain filterChain) {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;

        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            throw new JwtException("False Token");
        }

        jwt = authHeader.substring(7);

        String email = jwtService.extractUsername(jwt);

        Optional<User> user = userRepository.findByEmail(email);

        if(user.isEmpty()){
            throw new CustomerNotFound("Account Not Found");
        }

        List<Product> products = productRepository.findWishlistProductByUserId(user.get().getId());

        return mapper.mapObjects(products, ProductDTO.class);
    }

    @Override
    public ResponseEntity<?> updateUserAdditionalDetails(@NonNull HttpServletRequest request,
                                         @NonNull HttpServletResponse response,
                                         @NonNull FilterChain filterChain,
                                         RequestUpdateUserAdditionalDetails requestUpdateUserAdditionalDetails) {
        Map<String, String> body = new HashMap<>();

        final String authHeader = request.getHeader("Authorization");
        final String jwt;

        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            throw new JwtException("False Token");
        }

        jwt = authHeader.substring(7);

        String email = jwtService.extractUsername(jwt);

        Optional<User> user = userRepository.findByEmail(email);

        if(user.isEmpty()){
            throw new CustomerNotFound("Account Not Found");
        }

        UserAdditionalDetails userAdditionalDetails = mapper.mapObject(requestUpdateUserAdditionalDetails, UserAdditionalDetails.class);

        user.get().setUserAdditionalDetails(userAdditionalDetails);
        userRepository.save(user.get());

        body.put("response", "Success update user additional data");

        return ResponseEntity.status(HttpStatus.OK).body(body);
    }

    @Override
    public GetUserAdditionalDetails getUserAdditionalDetails(@NonNull HttpServletRequest request,
                                                             @NonNull HttpServletResponse response,
                                                             @NonNull FilterChain filterChain){
        final String authHeader = request.getHeader("Authorization");
        final String jwt;

        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            throw new JwtException("False Token");
        }

        jwt = authHeader.substring(7);

        String email = jwtService.extractUsername(jwt);

        Optional<User> user = userRepository.findByEmail(email);

        if(user.isEmpty()){
            throw new CustomerNotFound("Account Not Found");
        }

        return mapper.mapObject(user.get().getUserAdditionalDetails(), GetUserAdditionalDetails.class);
    }
}
