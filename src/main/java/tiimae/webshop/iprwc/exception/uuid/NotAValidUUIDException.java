package tiimae.webshop.iprwc.exception.uuid;

public class NotAValidUUIDException extends Exception {

    private final static String message = "This is not a valid UUID!";

    public NotAValidUUIDException() {
        super(message);
    }

}
