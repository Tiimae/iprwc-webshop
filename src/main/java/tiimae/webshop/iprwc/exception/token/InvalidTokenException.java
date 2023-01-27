package tiimae.webshop.iprwc.exception.token;

public class InvalidTokenException extends Exception {

   private static final String message = "There is an invalid token!";
   

   public InvalidTokenException() {
      super(message);
   }
}
