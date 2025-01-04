package it.unitn.userapi.api;

import it.unitn.userapi.openapi.api.AuthApiDelegate;
import it.unitn.userapi.openapi.model.AuthenticationRequestModel;
import it.unitn.userapi.openapi.model.AuthenticationResponseModel;
import it.unitn.userapi.openapi.model.RegisterRequestModel;
import it.unitn.userapi.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthApiDelegateImpl implements AuthApiDelegate {

    private final AuthenticationService authenticationService;

    @Override
    public ResponseEntity<AuthenticationResponseModel> login(AuthenticationRequestModel authenticationRequestModel) {
        log.debug("Trying to authenticate user...");
        log.trace("Authenticating user: [{}]", authenticationRequestModel);
        return ResponseEntity.ok(authenticationService.authenticate(authenticationRequestModel));
    }

    @Override
    public ResponseEntity<Void> refresh() {
        log.debug("Trying to refresh token...");
//        authenticationService.refreshToken(request, response);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<AuthenticationResponseModel> register(RegisterRequestModel registerRequestModel) {
        log.debug("Trying to register user...");
        log.trace("Registering user: [{}]", registerRequestModel);
        return ResponseEntity.ok(authenticationService.register(registerRequestModel));
    }
}
