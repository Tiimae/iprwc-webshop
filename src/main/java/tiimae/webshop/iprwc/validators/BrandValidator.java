package tiimae.webshop.iprwc.validators;

import org.springframework.stereotype.Component;

import tiimae.webshop.iprwc.DTO.BrandDTO;
import tiimae.webshop.iprwc.exception.InvalidDtoException;

@Component
public class BrandValidator extends Validator {

    public void validateDTO(BrandDTO brandDTO) throws InvalidDtoException {

        if (brandDTO.getBrandName() == null) {
            throw new InvalidDtoException("Brand name can't be null");
        }

        if (brandDTO.getWebPage() == null) {
            throw new InvalidDtoException("Web page can't be null");
        }

    }
}
