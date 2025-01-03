package it.unitn.userapi.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Setter
@Getter
@ConfigurationProperties(prefix = "app.environment")
public class AppConfig {
    private String secretKey;
    private Integer jwtExpireSeconds;
    private String carRentalApiUrl;
    private String carRentalApiUsername;
    private String carRentalApiPassword;
}
