package it.unitn.userapi.facade;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatusCode;

@Builder
public class HttpClientErrorsAwareResponse<T> {
    @Getter
    private HttpStatusCode statusCode;
    @Getter
    private T body;
    @Getter
    private String error;

    public boolean isSuccess() {
        return statusCode.is2xxSuccessful();
    }
}
