package com.Salora.SaloraWebService.Services.AuthenticationServices;

import com.Salora.SaloraWebService.Config.JwtAuth.JwtService;
import com.Salora.SaloraWebService.DTO.RequestDTO.AuthenticationRequestUserDTO;
import com.Salora.SaloraWebService.DTO.RequestDTO.RegisterRequestUserDTO;
import com.Salora.SaloraWebService.DTO.ResponseDTO.AutheticationResponseDTO;
import com.Salora.SaloraWebService.Model.Enums.TokenType;
import com.Salora.SaloraWebService.Model.Token.UserToken;
import com.Salora.SaloraWebService.Model.User;
import com.Salora.SaloraWebService.Repository.Token.TokenRepository;
import com.Salora.SaloraWebService.Repository.UserRepository;
import com.Salora.SaloraWebService.Utils.ProductMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class AuthService implements IAuthService{

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private final ProductMapper mapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public AuthService(ProductMapper productMapper) {
        this.mapper = productMapper;
    }

    @Override
    public AutheticationResponseDTO register(RegisterRequestUserDTO requestUserDTO) {
        Map<String, Object> payloadClaims = new HashMap<>();

        User user = mapper.mapObject(requestUserDTO, User.class);
        user.setPassword(passwordEncoder.encode(requestUserDTO.getPassword()));
        user.setRole(requestUserDTO.getRole());
        user = userRepository.save(user);

        Map<String, String> userPayloads = new HashMap<>();
        userPayloads.put("s_id", user.getId());
        userPayloads.put("s_uname", user.getName());

        payloadClaims.put("users", userPayloads);
        payloadClaims.put("authorities", user.getAuthorities());

        String jwtToken = jwtService.generateToken(payloadClaims, user);
        String refreshToken = jwtService.generateRefreshToken(user);
        this.saveUserToken(user, jwtToken);

        return new AutheticationResponseDTO(
                jwtToken,
                refreshToken
        );
    }

    @Override
    public AutheticationResponseDTO authentication(AuthenticationRequestUserDTO requestUserDTO) {
        Map<String, Object> payloadClaims = new HashMap<>();

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        requestUserDTO.getEmail(),
                        requestUserDTO.getPassword()
                )
        );
        Optional<User> user = Optional.of(userRepository.findByEmail(requestUserDTO.getEmail())
                .orElseThrow());

        Map<String, String> userPayloads = new HashMap<>();
        userPayloads.put("s_id", user.get().getId());
        userPayloads.put("s_uname", user.get().getName());

        payloadClaims.put("users", userPayloads);
        payloadClaims.put("authorities", user.get().getAuthorities());

        String jwtToken = jwtService.generateToken(payloadClaims, user.get());
        String refreshToken = jwtService.generateRefreshToken(user.get());
        this.revokeAllUserTokens(user.get());
        this.saveUserToken(user.get(), jwtToken);

        return new AutheticationResponseDTO(
                jwtToken,
                refreshToken
        );
    }

    @Override
    public boolean validate(@NonNull HttpServletRequest request,
                            @NonNull HttpServletResponse response,
                            @NonNull FilterChain filterChain){
        final String authHeader = request.getHeader("Authorization");
        final String jwt;

        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            return false;
        }

        jwt = authHeader.substring(7);

        return tokenRepository.findByToken(jwt)
                .map(t -> !t.isExpired() && !t.isRevoked())
                .orElse(false);
    }

    @Override
    public Claims getSession(@NonNull HttpServletRequest request,
                                         @NonNull HttpServletResponse response,
                                         @NonNull FilterChain filterChain){
        final String authHeader = request.getHeader("Authorization");
        final String jwt;

        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            throw new JwtException("False Token");
        }

        jwt = authHeader.substring(7);

        return jwtService.extractPayloads(jwt);
    }

    private void saveUserToken(User user, String jwtToken) {
        UserToken token = new UserToken();
        token.setUserEntity(user);
        token.setToken(jwtToken);
        token.setTokenType(TokenType.BEARER);
        token.setExpired(false);
        token.setRevoked(false);
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        List<UserToken> validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }
}
