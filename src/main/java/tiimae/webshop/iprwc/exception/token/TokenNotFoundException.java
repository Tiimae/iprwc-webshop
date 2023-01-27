package tiimae.webshop.iprwc.exception.token;

public class TokenNotFoundException extends Exception {

    private static final String message = "Token not found.";

    public TokenNotFoundException() {
        super(message);
    }
}
