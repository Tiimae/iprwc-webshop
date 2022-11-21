package tiimae.webshop.iprwc.service;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class ApiResponseService<Type> {
    private long code;
    private Type payload;
    private String message;

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

