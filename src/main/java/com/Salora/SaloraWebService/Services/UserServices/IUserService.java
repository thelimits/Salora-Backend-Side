package com.Salora.SaloraWebService.Services.UserServices;

import com.Salora.SaloraWebService.DTO.ProductDTO.ProductDTO;
import com.Salora.SaloraWebService.DTO.RequestDTO.RequestUpdateUserAdditionalDetails;
import com.Salora.SaloraWebService.DTO.ResponseDTO.GetUserAdditionalDetails;
import com.Salora.SaloraWebService.DTO.ResponseDTO.GetUserProfileDTO;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface IUserService {
    CompletableFuture<GetUserProfileDTO> getUserProfile(@NonNull HttpServletRequest request,
                                                       @NonNull HttpServletResponse response,
                                                       @NonNull FilterChain filterChain) throws ServletException, IOException;

    ResponseEntity<?> addWishlist(@NonNull HttpServletRequest request,
                                  @NonNull HttpServletResponse response,
                                  @NonNull FilterChain filterChain,
                                  String id) throws ServletException, IOException;

    ResponseEntity<?> deleteWhislist(@NonNull HttpServletRequest request,
                                      @NonNull HttpServletResponse response,
                                      @NonNull FilterChain filterChain,
                                      String id) throws ServletException, IOException;

    ResponseEntity<?> checkWishlists(@NonNull HttpServletRequest request,
                                     @NonNull HttpServletResponse response,
                                     @NonNull FilterChain filterChain) throws ServletException, IOException;


    List<ProductDTO> getWishlist(@NonNull HttpServletRequest request,
                                 @NonNull HttpServletResponse response,
                                 @NonNull FilterChain filterChain) throws ServletException, IOException;

    ResponseEntity<?> updateUserAdditionalDetails(@NonNull HttpServletRequest request,
                                                  @NonNull HttpServletResponse response,
                                                  @NonNull FilterChain filterChain,
                                                  RequestUpdateUserAdditionalDetails requestUpdateUserAdditionalDetails) throws ServletException, IOException;

    GetUserAdditionalDetails getUserAdditionalDetails (@NonNull HttpServletRequest request,
                                                       @NonNull HttpServletResponse response,
                                                       @NonNull FilterChain filterChain) throws ServletException, IOException;
}
