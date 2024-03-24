package com.Salora.SaloraWebService.Controller;

import com.Salora.SaloraWebService.DTO.RequestDTO.AuthenticationRequestUserDTO;
import com.Salora.SaloraWebService.DTO.RequestDTO.RegisterRequestUserDTO;
import com.Salora.SaloraWebService.Services.AuthenticationServices.IAuthService;
import com.Salora.SaloraWebService.Services.RequestServiceAsync;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping(value = "/api/v1/auth/")
public class AuthenticationController {

    @Autowired
    private IAuthService authService;

    @Autowired
    private RequestServiceAsync requestServiceAsync;

    @PostMapping("register")
    @Hidden
    public CompletableFuture<ResponseEntity<?>> register(
            @RequestBody RegisterRequestUserDTO requestUserDTO
    ){
        return requestServiceAsync.processRequest(() -> authService.register(requestUserDTO));
    }

    @PostMapping("authenticate")
    @Hidden
    public CompletableFuture<ResponseEntity<?>> auth(
            @RequestBody AuthenticationRequestUserDTO requestUserDTO
    ){
        return requestServiceAsync.processRequest(() -> authService.authentication(requestUserDTO));
    }

    @PostMapping("validate")
    @Hidden
    @PreAuthorize("hasRole('ADMIN') or hasRole('CUSTOMER')")
    public CompletableFuture<ResponseEntity<?>> validate(HttpServletRequest request,
                                                         HttpServletResponse response,
                                                         FilterChain filterChain) {
        return requestServiceAsync.processRequest(() -> {
            try {
                boolean isValid = authService.validate(request, response, filterChain);
                if (isValid) {
                    return ResponseEntity.ok().build();
                } else {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
                }
            } catch (ServletException | IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @PostMapping("session")
    @Hidden
    @PreAuthorize("hasRole('ADMIN') or hasRole('CUSTOMER')")
    public CompletableFuture<ResponseEntity<?>> getSessions(HttpServletRequest request,
                                                            HttpServletResponse response,
                                                            FilterChain filterChain) {

        return requestServiceAsync.processRequest(() -> {
            try {
                return authService.getSession(request, response, filterChain);
            } catch (ServletException | IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

}
