package tiimae.webshop.iprwc.validators;

import org.springframework.stereotype.Component;

import tiimae.webshop.iprwc.DTO.CategoryDTO;
import tiimae.webshop.iprwc.exception.InvalidDtoException;

@Component
public class CategoryValidator extends Validator {

    public void validateDTO(CategoryDTO categoryDTO) throws InvalidDtoException {

        if (categoryDTO.getCategoryName() == null) {
            throw new InvalidDtoException("Category name can't be null");
        }

    }

}
