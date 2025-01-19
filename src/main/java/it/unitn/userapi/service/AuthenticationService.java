package it.unitn.userapi.service;

import it.unitn.userapi.openapi.model.AuthenticationRequestModel;
import it.unitn.userapi.openapi.model.AuthenticationResponseModel;

import java.util.Optional;

public interface AuthenticationService {
    Optional<AuthenticationResponseModel> authenticate(AuthenticationRequestModel request);
}
