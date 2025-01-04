package it.unitn.userapi.service;

import it.unitn.userapi.openapi.model.AuthenticationRequestModel;
import it.unitn.userapi.openapi.model.AuthenticationResponseModel;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface AuthenticationService {
    AuthenticationResponseModel authenticate(AuthenticationRequestModel request);
    void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;
}
