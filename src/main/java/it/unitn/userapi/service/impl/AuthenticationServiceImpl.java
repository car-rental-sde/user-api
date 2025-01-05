package it.unitn.userapi.service.impl;

import it.unitn.userapi.entity.UserEntity;
import it.unitn.userapi.openapi.model.AuthenticationRequestModel;
import it.unitn.userapi.openapi.model.AuthenticationResponseModel;
import it.unitn.userapi.repository.UserRepository;
import it.unitn.userapi.security.JwtService;
import it.unitn.userapi.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
//    private final TokenRepository tokenRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public Optional<AuthenticationResponseModel> authenticate(AuthenticationRequestModel request) {
        Optional<UserEntity> userOptional = userRepository.findByUsername(request.getUsername());
        if (userOptional.isEmpty()) {
            log.warn("User not found: [{}]", request.getUsername());
            return Optional.empty();
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        UserEntity user = userOptional.get();
        var jwtToken = jwtService.generateToken(user.getUsername());
//        var refreshToken = jwtService.generateRefreshToken(user);

//        revokeAllUserTokens(user); // TODO: What's this?
//        saveUserToken(user, jwtToken); // TODO: What's this?

        return Optional.ofNullable(AuthenticationResponseModel.builder()
                .accessToken(jwtToken)
//                .refreshToken(refreshToken)
                .build());
    }

//    private void saveUserToken(UserEntity user, String jwtToken) {
//        var token = Token.builder()
//                .user(user)
//                .token(jwtToken)
//                .tokenType(TokenType.BEARER)
//                .expired(false)
//                .revoked(false)
//                .build();
//
//        tokenRepository.save(token);
//    }

//    private void revokeAllUserTokens(UserEntity user) {
//        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
//
//        if (validUserTokens.isEmpty()) {
//            return;
//        }
//
//        validUserTokens.forEach(token -> {
//            token.setExpired(true);
//            token.setRevoked(true);
//        });
//        tokenRepository.saveAll(validUserTokens);
//    }

    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
//        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
//        final String refreshToken;
//        final String userEmail;
//
//        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
//            return;
//        }
//
//        refreshToken = authHeader.substring(7);
//        userEmail = jwtService.extractUsername(refreshToken);
//
//        if (userEmail != null) {
//            var user = this.userRepository.findByEmail(userEmail)
//                    .orElseThrow();
//            if (jwtService.isTokenValid(refreshToken, user)) {
//                var accessToken = jwtService.generateToken(user);
//                revokeAllUserTokens(user);
//                saveUserToken(user, accessToken);
//                var authResponse = AuthenticationResponse.builder()
//                        .accessToken(accessToken)
//                        .refreshToken(refreshToken)
//                        .build();
//                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
//            }
//        }
    }
}
