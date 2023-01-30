package tiimae.webshop.iprwc.exception.token;

public class TokenAlreadyExistsException extends Exception {
   
   private static final String message = "Token already exists.";

    public TokenAlreadyExistsException() {
        super(message);
    }

}
