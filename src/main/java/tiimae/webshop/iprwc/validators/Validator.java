package tiimae.webshop.iprwc.validators;

import java.util.UUID;
import java.util.regex.Pattern;

import tiimae.webshop.iprwc.DAO.ProductDAO;
import tiimae.webshop.iprwc.models.Product;

public class Validator {
    protected final Pattern UUID_REGEX_PATTERN = Pattern.compile("^[{]?[0-9a-fA-F]{8}-([0-9a-fA-F]{4}-){3}[0-9a-fA-F]{12}[}]?$");
    protected final Pattern URL_REGEX_PATTERN = Pattern.compile("\\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]");
    protected static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    protected ProductDAO productDAO;

    public Validator(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    public String checkIfStringIsUUID(String id) {
        if (!this.UUID_REGEX_PATTERN.matcher(id).matches()) {
            return id + " is not a valid UUID";
        }

        return null;
    }
    

    public String CheckIfProductExists(UUID id) {
        Product product = this.productDAO.get(id);

         if (product == null) {
            return "The product with the id " + id + " doesn't exist.";
         }

         return null;
    }
}
