package tiimae.webshop.iprwc.validators;

import java.util.UUID;
import java.util.regex.Pattern;

import tiimae.webshop.iprwc.DAO.ProductDAO;
import tiimae.webshop.iprwc.exception.uuid.NotAValidUUIDException;
import tiimae.webshop.iprwc.models.Product;

public abstract class Validator {
    protected final Pattern UUID_REGEX_PATTERN = Pattern.compile("^[{]?[0-9a-fA-F]{8}-([0-9a-fA-F]{4}-){3}[0-9a-fA-F]{12}[}]?$");
    protected final Pattern URL_REGEX_PATTERN = Pattern.compile("\\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]");
    protected static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    protected static final Pattern VALID_PASSWORD_REGEX = Pattern.compile("(?=([^a-z]*[a-z]){1,})(?=([^A-Z]*[A-Z]){1,})(?=([^0-9]*[0-9]){1,})(?=(.*[$@$!%*?&#]){1,})[A-Za-z\\d$@$!%*?&#.]{8,}");

    public UUID checkIfStringIsUUID(String id) throws NotAValidUUIDException {
        if (!this.UUID_REGEX_PATTERN.matcher(id).matches()) {
            throw new NotAValidUUIDException();
        }

        return UUID.fromString(id);
    }
}
