package com.Salora.SaloraWebService.Services.AuthenticationServices;

import com.Salora.SaloraWebService.DTO.RequestDTO.AuthenticationRequestUserDTO;
import com.Salora.SaloraWebService.DTO.RequestDTO.RegisterRequestUserDTO;
import com.Salora.SaloraWebService.DTO.ResponseDTO.AutheticationResponseDTO;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;

import java.io.IOException;

public interface IAuthService {
    AutheticationResponseDTO register( RegisterRequestUserDTO requestUserDTO);

    AutheticationResponseDTO authentication( AuthenticationRequestUserDTO requestUserDTO);

    boolean validate(@NonNull HttpServletRequest request,
                     @NonNull HttpServletResponse response,
                     @NonNull FilterChain filterChain) throws ServletException, IOException;

    Claims getSession(@NonNull HttpServletRequest request,
                      @NonNull HttpServletResponse response,
                      @NonNull FilterChain filterChain)throws ServletException, IOException;
}
