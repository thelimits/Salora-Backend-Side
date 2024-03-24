package com.Salora.SaloraWebService.Services.CartServices;

import com.Salora.SaloraWebService.Config.JwtAuth.JwtService;
import com.Salora.SaloraWebService.DTO.ResponseDTO.GetCartResponseDTO;
import com.Salora.SaloraWebService.Exception.CustomerNotFound;
import com.Salora.SaloraWebService.Model.Cart;
import com.Salora.SaloraWebService.Model.Enums.SizeProduct.Size;
import com.Salora.SaloraWebService.Model.Product;
import com.Salora.SaloraWebService.Model.ProductAttribute;
import com.Salora.SaloraWebService.Model.User;
import com.Salora.SaloraWebService.Repository.CartRepository;
import com.Salora.SaloraWebService.Repository.ProductRepository;
import com.Salora.SaloraWebService.Repository.UserRepository;
import com.Salora.SaloraWebService.Utils.ProductMapper;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.*;

@Service
@Transactional
@Slf4j
public class CartService implements ICartService{
    Logger logger = LoggerFactory.getLogger(CartService.class);

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductMapper mapper;

    @Autowired
    private JwtService jwtService;

    @Override
    public ResponseEntity<?> addCart(@NonNull HttpServletRequest request,
                                     @NonNull HttpServletResponse response,
                                     @NonNull FilterChain filterChain,
                                     String id, Size size, Integer quantity) {
        Cart cart = new Cart();
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
        CompletableFuture<Optional<Product>> productFuture = CompletableFuture.supplyAsync(() -> productRepository.findProductByIdAndSize(id, size), executorService);

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

        ProductAttribute productAttribute = product.get().getAttributes().stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Product attribute not found"));

        cart.setUser(user.get());
        cart.setProduct(product.get());
        cart.setProductAttribute(productAttribute);
        cart.setQuantity(quantity);

        cartRepository.save(cart);

        body.put("message", "Product add to cart successful");
        return ResponseEntity.status(HttpStatus.OK).body(body);
    }

    @Override
    public List<GetCartResponseDTO> getCart(@NonNull HttpServletRequest request,
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

        List<Product> products = productRepository.findCartProductByUserId(user.get().getId());

        return mapper.mapObjects(products, GetCartResponseDTO.class);
    }
}
