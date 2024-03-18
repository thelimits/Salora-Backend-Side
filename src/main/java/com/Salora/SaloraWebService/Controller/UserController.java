package com.Salora.SaloraWebService.Controller;

import com.Salora.SaloraWebService.Services.RequestServiceAsync;
import com.Salora.SaloraWebService.Services.UserServices.IUserService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

@RestController
@CrossOrigin
@RequestMapping(value = "/account")
@Api(tags = "User Controller")
public class UserController {

    @Autowired
    private IUserService iUserService;

    @Autowired
    private RequestServiceAsync requestServiceAsync;

    @GetMapping("/profile")
    @Hidden
    @PreAuthorize("hasRole('ADMIN') or hasRole('CUSTOMER')")
    public CompletableFuture<ResponseEntity<?>> getProfile(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ){
        return requestServiceAsync.processRequest(() -> {
            try {
                return iUserService.getUserProfile(request, response, filterChain);
            } catch (ServletException | IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
