package tiimae.webshop.iprwc.service;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiResponseService<Type> {
    private final long code;
    private Type payload;
    private final String message;

    public ApiResponseService(HttpStatus code, Type payload) {
        this.code = code.value();
        this.message = code.getReasonPhrase();
        this.payload = payload;
    }

    public ApiResponseService(HttpStatus code, String message) {
        this.code = code.value();
        this.message = message;
    }
}

