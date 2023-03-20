package tiimae.webshop.iprwc.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.firewall.RequestRejectedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import tiimae.webshop.iprwc.exception.EntryAlreadyExistsException;
import tiimae.webshop.iprwc.exception.EntryNotFoundException;
import tiimae.webshop.iprwc.exception.InvalidDtoException;
import tiimae.webshop.iprwc.exception.token.TokenNotFoundException;
import tiimae.webshop.iprwc.exception.uuid.NotAValidUUIDException;
import tiimae.webshop.iprwc.service.response.ApiResponseService;

import javax.persistence.EntityExistsException;
import java.nio.file.AccessDeniedException;

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
            EntryAlreadyExistsException.class
    })
    public ResponseEntity<Object> handleBadRequestException(Exception e, WebRequest request) {
        return handleExceptionInternal(e, new ApiResponseService<>(HttpStatus.BAD_REQUEST, e.getMessage()), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler({
            AccessDeniedException.class
    })
    public ResponseEntity<Object> handleForbiddenException(Exception e, WebRequest request) {
        return handleExceptionInternal(e, new ApiResponseService<>(HttpStatus.FORBIDDEN, e.getMessage()), new HttpHeaders(), HttpStatus.FORBIDDEN, request);
    }

    @ExceptionHandler({
            RequestRejectedException.class
    })
    public ResponseEntity<Object> handleInternalServerErrorException(Exception e, WebRequest request) {
        return handleExceptionInternal(e, new ApiResponseService<>(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

}
