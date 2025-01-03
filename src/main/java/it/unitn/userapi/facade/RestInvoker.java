package it.unitn.userapi.facade;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;

import java.util.function.Supplier;

@Slf4j
public class RestInvoker<T> {
    public HttpClientErrorsAwareResponse<T> invoke(Supplier<ResponseEntity<T>> supplier) {
        try {
            ResponseEntity<T> t = supplier.get();
            return HttpClientErrorsAwareResponse.<T>builder()
                    .statusCode(t.getStatusCode())
                    .body(t.getBody())
                    .build();
        } catch (HttpClientErrorException hce) {
            String responseBody = hce.getResponseBodyAsString();
            log.debug("Handling 4xx series http error, status [{}], error message [{}]", hce.getStatusCode(), responseBody);
            return HttpClientErrorsAwareResponse.<T>builder()
                    .statusCode(hce.getStatusCode())
                    .error(responseBody)
                    .build();
        } catch (RestClientException rce) {
            log.error("Rest client error", rce);
            throw rce;
        }
    }
}
