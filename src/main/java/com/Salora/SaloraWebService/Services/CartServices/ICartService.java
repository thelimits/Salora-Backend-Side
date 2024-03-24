package com.Salora.SaloraWebService.Services.CartServices;

import com.Salora.SaloraWebService.DTO.ProductDTO.ProductDTO;
import com.Salora.SaloraWebService.DTO.ResponseDTO.GetCartResponseDTO;
import com.Salora.SaloraWebService.DTO.ResponseDTO.GetProductDetailsResponseDTO;
import com.Salora.SaloraWebService.Model.Enums.SizeProduct.Size;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;

import java.io.IOException;
import java.util.List;

public interface ICartService {
    ResponseEntity<?> addCart(@NonNull HttpServletRequest request,
                                         @NonNull HttpServletResponse response,
                                         @NonNull FilterChain filterChain,
                                         String id, Size size, Integer quantity) throws ServletException, IOException;

    List<GetCartResponseDTO> getCart(@NonNull HttpServletRequest request,
                                         @NonNull HttpServletResponse response,
                                         @NonNull FilterChain filterChain) throws ServletException, IOException;
}
