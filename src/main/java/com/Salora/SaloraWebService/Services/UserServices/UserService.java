package com.Salora.SaloraWebService.Services.UserServices;

import com.Salora.SaloraWebService.Config.JwtAuth.JwtService;
import com.Salora.SaloraWebService.DTO.ResponseDTO.GetUserProfileDTO;
import com.Salora.SaloraWebService.Exception.CustomerNotFound;
import com.Salora.SaloraWebService.Model.User;
import com.Salora.SaloraWebService.Repository.UserRepository;
import com.Salora.SaloraWebService.Utils.ProductMapper;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
public class UserService implements IUserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductMapper mapper;

    @Autowired
    private JwtService jwtService;

    @Override
    public GetUserProfileDTO getUserProfile(@NonNull HttpServletRequest request,
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
            throw new CustomerNotFound("Customer Not Found");
        }

        return mapper.mapObject(user.get(), GetUserProfileDTO.class);
    }
}
