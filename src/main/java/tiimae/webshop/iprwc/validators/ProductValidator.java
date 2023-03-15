package tiimae.webshop.iprwc.validators;

import org.springframework.stereotype.Component;
import tiimae.webshop.iprwc.DTO.ProductDTO;
import tiimae.webshop.iprwc.exception.InvalidDtoException;

@Component
public class ProductValidator extends Validator {

    public void validateDTO(ProductDTO productDTO) throws InvalidDtoException {
        if (productDTO.getName() == null) {
            throw new InvalidDtoException("Product name must be specified");
        }

        if (productDTO.getPrice() == null) {
            throw new InvalidDtoException("Product price must be specified");
        }

        if (productDTO.getPrice() < 0.01) {
            throw new InvalidDtoException("Price is to low!");
        }

        if (productDTO.getDescription() == null) {
            throw new InvalidDtoException("Product description must be specified");
        }

        if (productDTO.getBrandId() == null) {
            throw new InvalidDtoException("Brand must be specified");
        }

        if (productDTO.getSupplierId() == null) {
            throw new InvalidDtoException("Supplier must be specified");
        }

        if (productDTO.getCategoryId() == null) {
            throw new InvalidDtoException("Category must be specified");
        }
    }

}
