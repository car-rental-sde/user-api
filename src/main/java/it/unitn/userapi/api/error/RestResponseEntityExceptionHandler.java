package it.unitn.userapi.api.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Slf4j
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {AlternativeHttpStatusCodeResponse.class})
    protected ResponseEntity<Object> handleAlternativeResponse(RuntimeException exception, WebRequest webRequest) {
        AlternativeHttpStatusCodeResponse alternativeResponse = (AlternativeHttpStatusCodeResponse) exception;
        ServletWebRequest request = (ServletWebRequest) webRequest;
        log.debug("Alternative response of code [{}] and response type [{}] for request [{}:{}]",
                alternativeResponse.getStatusCode(), alternativeResponse.getBody() != null ? alternativeResponse.getBody().getClass().getName() : "void",
                request.getHttpMethod(), request);
        return ResponseEntity.status(alternativeResponse.getStatusCode()).body(alternativeResponse.getBody());
    }
}
