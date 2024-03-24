package com.Salora.SaloraWebService.Controller;

import com.Salora.SaloraWebService.DTO.RequestDTO.RequestUpdateUserAdditionalDetails;
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
import java.util.concurrent.ExecutionException;

@RestController
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
                return iUserService.getUserProfile(request, response, filterChain).get();
            } catch (ServletException | IOException | ExecutionException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @PostMapping("/customer/product/wishlist")
    @Hidden
    @PreAuthorize("hasRole('CUSTOMER')")
    public CompletableFuture<ResponseEntity<?>> addProductWishlist(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain,
            @RequestParam String id
    ){
        return requestServiceAsync.processRequest(() -> {
            try {
                return iUserService.addWishlist(request, response, filterChain, id);
            } catch (ServletException | IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @DeleteMapping("/customer/product/wishlist")
    @Hidden
    @PreAuthorize("hasRole('CUSTOMER')")
    public CompletableFuture<ResponseEntity<?>> deleteWishlist(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain,
            @RequestParam String id
    ){
        return requestServiceAsync.processRequest(() -> {
            try {
                return iUserService.deleteWhislist(request, response, filterChain, id);
            } catch (ServletException | IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @GetMapping("/customer/product/wishlist-check")
    @Hidden
    @PreAuthorize("hasRole('CUSTOMER')")
    public CompletableFuture<ResponseEntity<?>> checkWishlist(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ){
        return requestServiceAsync.processRequest(() -> {
            try {
                return iUserService.checkWishlists(request, response, filterChain);
            } catch (ServletException | IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @GetMapping("/customer/product/wishlist")
    @Hidden
    @PreAuthorize("hasRole('CUSTOMER')")
    public CompletableFuture<ResponseEntity<?>> getProductWishlists(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ){
        return requestServiceAsync.processRequest(() -> {
            try {
                return iUserService.getWishlist(request, response, filterChain);
            } catch (ServletException | IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @PutMapping("/customer/additional-details")
    @Hidden
    @PreAuthorize("hasRole('CUSTOMER')")
    public CompletableFuture<ResponseEntity<?>> updateUserAdditionalDetails(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain,
            @RequestBody RequestUpdateUserAdditionalDetails requestUpdateUserAdditionalDetails
            ){
        return requestServiceAsync.processRequest(() -> {
            try {
                return iUserService.updateUserAdditionalDetails(request, response, filterChain, requestUpdateUserAdditionalDetails);
            } catch (ServletException | IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @GetMapping("/customer/additional-details")
    @Hidden
    @PreAuthorize("hasRole('CUSTOMER')")
    public CompletableFuture<ResponseEntity<?>> getUserAdditionalDetails(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ){
        return requestServiceAsync.processRequest(() -> {
            try {
                return iUserService.getUserAdditionalDetails(request, response, filterChain);
            } catch (ServletException | IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
