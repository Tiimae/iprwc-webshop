package tiimae.webshop.iprwc.exception.token;

public class TokenExpiredException extends Exception {
   private static final String message = "Token is expired!";

   public TokenExpiredException() {
      super(message);
   }
}
