package it.unitn.userapi.config;

import it.unitn.userapi.carrentalapi.ApiClient;
import it.unitn.userapi.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class CarRentalApiConfig {

    private final AppConfig appConfig;
    private final JwtService jwtService;

    @Bean("CarRentalAPIClient")
    @Primary
    ApiClient apiClient(RestTemplate restTemplate) {
        ApiClient apiClient = new SecuredApiClient(restTemplate);
        apiClient.setBasePath(appConfig.getCarRentalApiUrl());
        return apiClient;
    }

    private class SecuredApiClient extends ApiClient {

        public SecuredApiClient(RestTemplate restTemplate) {
            super(restTemplate);
        }

        @Override
        public <T> ResponseEntity<T> invokeAPI(String path,
                                               HttpMethod method,
                                               Map<String, Object> pathParams,
                                               MultiValueMap<String, String> queryParams,
                                               Object body,
                                               HttpHeaders headerParams,
                                               MultiValueMap<String, String> cookieParams,
                                               MultiValueMap<String, Object> formParams,
                                               List<MediaType> accept,
                                               MediaType contentType,
                                               String[] authNames,
                                               ParameterizedTypeReference<T> returnType) throws RestClientException {
            String username = appConfig.getCarRentalApiUsername();
            String password = appConfig.getCarRentalApiPassword();
//            headerParams.add("Authorization", "Basic " + encodeCredentials(username, password));

            // Generate a jwt token
            headerParams.add("Authorization", "Bearer " + jwtService.generateToken(appConfig.getCarRentalApiUsername()));
            return super.invokeAPI(path, method, pathParams, queryParams, body, headerParams, cookieParams, formParams, accept, contentType, authNames, returnType);
        }
    }

//    private String encodeCredentials(String username, String password) {
//        String credentials = username + ":" + password;
//        byte[] credentialsBytes = credentials.getBytes(StandardCharsets.UTF_8);
//        return Base64.getEncoder().encodeToString(credentialsBytes);
//    }
}
