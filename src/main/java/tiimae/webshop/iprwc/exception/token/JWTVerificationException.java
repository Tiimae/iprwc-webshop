package tiimae.webshop.iprwc.exception.token;

public class JWTVerificationException extends Exception {
   private static final String message = "Token has expired.";

    public JWTVerificationException() {
        super(message);
    }
}
