package tiimae.webshop.iprwc.validators;

import org.springframework.stereotype.Component;
import tiimae.webshop.iprwc.DTO.CartDTO;
import tiimae.webshop.iprwc.exception.InvalidDtoException;

@Component
public class CartValidator extends Validator {

    public void validateDTO(CartDTO cartDTO) throws InvalidDtoException {
        if (cartDTO.getQuantity() == null) {
            throw new InvalidDtoException("Quantity must be specified");
        }

        if (cartDTO.getProductId() == null) {
            throw new InvalidDtoException("Product id must be specified");
        }

        if (cartDTO.getUserId() == null) {
            throw new InvalidDtoException("User id must be specified");
        }

        if (cartDTO.getQuantity() < 1) {
            throw new InvalidDtoException("Quantity is to low");
        }
    }
}
