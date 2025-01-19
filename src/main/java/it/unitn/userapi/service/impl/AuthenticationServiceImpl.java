package it.unitn.userapi.service.impl;

import it.unitn.userapi.entity.UserEntity;
import it.unitn.userapi.openapi.model.AuthenticationRequestModel;
import it.unitn.userapi.openapi.model.AuthenticationResponseModel;
import it.unitn.userapi.repository.UserRepository;
import it.unitn.userapi.security.JwtService;
import it.unitn.userapi.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
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

        return Optional.ofNullable(AuthenticationResponseModel.builder()
                .accessToken(jwtToken)
                .build());
    }
}
