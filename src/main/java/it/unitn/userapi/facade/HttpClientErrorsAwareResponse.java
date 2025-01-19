package it.unitn.userapi.facade;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatusCode;

@Getter
@Builder
public class HttpClientErrorsAwareResponse<T> {
    private HttpStatusCode statusCode;
    private T body;
    private String error;

    public boolean isSuccess() {
        return statusCode.is2xxSuccessful();
    }
}
