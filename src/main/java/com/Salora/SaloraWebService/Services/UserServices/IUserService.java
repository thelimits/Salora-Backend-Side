package com.Salora.SaloraWebService.Services.UserServices;

import com.Salora.SaloraWebService.DTO.ResponseDTO.GetUserProfileDTO;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;

import java.io.IOException;

public interface IUserService {
    GetUserProfileDTO getUserProfile(@NonNull HttpServletRequest request,
                                     @NonNull HttpServletResponse response,
                                     @NonNull FilterChain filterChain) throws ServletException, IOException;
}
