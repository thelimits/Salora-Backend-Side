package com.Salora.SaloraWebService.Config.JwtAuth;

import com.Salora.SaloraWebService.Model.Token.UserToken;
import com.Salora.SaloraWebService.Repository.Token.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Service
public class LogoutService implements LogoutHandler {

    @Autowired
    private TokenRepository tokenRepository;


    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            return;
        }
        jwt = authHeader.substring(7);

        UserToken storeToken = tokenRepository
                .findByToken(jwt)
                .orElse(null);

        if (storeToken != null) {
            storeToken.setExpired(true);
            storeToken.setRevoked(true);
            tokenRepository.save(storeToken);
            SecurityContextHolder.clearContext();
        }
    }
}
