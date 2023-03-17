package tiimae.webshop.iprwc.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.firewall.RequestRejectedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import tiimae.webshop.iprwc.exception.EntryNotFoundException;
import tiimae.webshop.iprwc.exception.InvalidDtoException;
import tiimae.webshop.iprwc.exception.token.TokenNotFoundException;
import tiimae.webshop.iprwc.exception.uuid.NotAValidUUIDException;
import tiimae.webshop.iprwc.service.response.ApiResponseService;

import javax.persistence.EntityExistsException;

@ControllerAdvice
public class ExceptionHandlerController extends ResponseEntityExceptionHandler {

    @ExceptionHandler({
            EntryNotFoundException.class,
            TokenNotFoundException.class
    })
    public ResponseEntity<Object> handleNotFoundException(Exception e, WebRequest request) {
        return handleExceptionInternal(e, new ApiResponseService<>(HttpStatus.NOT_FOUND, e.getMessage()), new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }


    @ExceptionHandler({
            EntityExistsException.class,
            NotAValidUUIDException.class,
            InvalidDtoException.class,
    })
    public ResponseEntity<Object> handleBadRequestException(Exception e, WebRequest request) {
        return handleExceptionInternal(e, new ApiResponseService<>(HttpStatus.BAD_REQUEST, e.getMessage()), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler({
            RequestRejectedException.class
    })
    public ResponseEntity<Object> handleInternalServerErrorException(Exception e, WebRequest request) {
        return handleExceptionInternal(e, new ApiResponseService<>(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

}
