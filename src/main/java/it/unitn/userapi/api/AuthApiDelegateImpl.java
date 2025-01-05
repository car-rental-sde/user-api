package it.unitn.userapi.api;

import it.unitn.userapi.openapi.api.AuthApiDelegate;
import it.unitn.userapi.openapi.model.AuthenticationRequestModel;
import it.unitn.userapi.openapi.model.AuthenticationResponseModel;
import it.unitn.userapi.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthApiDelegateImpl implements AuthApiDelegate {

    private final AuthenticationService authenticationService;

    @Override
    public ResponseEntity<AuthenticationResponseModel> login(AuthenticationRequestModel authenticationRequestModel) {
        log.debug("Trying to authenticate user...");
        log.trace("Authenticating user: [{}]", authenticationRequestModel);

        Optional<AuthenticationResponseModel> authenticationOptional = authenticationService.authenticate(authenticationRequestModel);

        return authenticationOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @Override
    public ResponseEntity<Void> refresh() {
        log.debug("Trying to refresh token...");
//        authenticationService.refreshToken(request, response);
        return ResponseEntity.ok().build();
    }
}
