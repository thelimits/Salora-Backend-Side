package com.Salora.SaloraWebService.Controller;

import com.Salora.SaloraWebService.Model.Enums.SizeProduct.Size;
import com.Salora.SaloraWebService.Services.CartServices.ICartService;
import com.Salora.SaloraWebService.Services.RequestServiceAsync;
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
@RequestMapping(value = "/cart")
@Api(tags = "Cart Controller")
@PreAuthorize("hasRole('CUSTOMER')")
public class CartController {

    @Autowired
    private ICartService iCartService;

    @Autowired
    private RequestServiceAsync requestServiceAsync;

    @PostMapping("/customer/add-product")
    @Hidden
    public CompletableFuture<ResponseEntity<?>> addProductCart(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain,
            @RequestParam String id,
            @RequestParam Size size,
            @RequestParam Integer qty
    ){
        return requestServiceAsync.processRequest(() -> {
            try {
                return iCartService.addCart(request, response, filterChain, id, size, qty);
            } catch (ServletException | IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @GetMapping()
    @Hidden
    public CompletableFuture<ResponseEntity<?>> addProductCart(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ){
        return requestServiceAsync.processRequest(() -> {
            try {
                return iCartService.getCart(request, response, filterChain);
            } catch (ServletException | IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
