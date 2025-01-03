package it.unitn.userapi.config;

import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.cookie.StandardCookieSpec;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Bean
    RestTemplate restTemplate() {

        HttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setCookieSpec(StandardCookieSpec.STRICT)
                        .build())
                .build();

        HttpComponentsClientHttpRequestFactory factory =
                new HttpComponentsClientHttpRequestFactory(httpClient);
        RestTemplate restTemplate = new RestTemplate(factory);

        //Write dates in ISO format
        restTemplate.getMessageConverters().stream()
                .filter(converter -> converter instanceof AbstractJackson2HttpMessageConverter)
                .map(converter -> (AbstractJackson2HttpMessageConverter) converter)
                .forEach(converter -> converter.getObjectMapper()
                        .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                );

        return restTemplate;
    }
}
