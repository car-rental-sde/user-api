package it.unitn.userapi.config;

import it.unitn.userapi.carrentalapi.ApiClient;
import it.unitn.userapi.carrentalapi.client.CarsApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiConfig {

    @Bean
    public CarsApi carsApi(ApiClient apiClient) {
        return new CarsApi(apiClient);
    }
}
