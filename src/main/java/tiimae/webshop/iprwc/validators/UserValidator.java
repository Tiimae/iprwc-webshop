package tiimae.webshop.iprwc.validators;

import org.springframework.stereotype.Component;
import tiimae.webshop.iprwc.DTO.UserDTO;
import tiimae.webshop.iprwc.exception.InvalidDtoException;

@Component
public class UserValidator extends Validator {

   public void validateDTO(UserDTO userDTO) throws InvalidDtoException {
      if (userDTO.getEmail() == null) {
         throw new InvalidDtoException("Email can not be null");
      }

      if (userDTO.getLastName() == null) {
         throw new InvalidDtoException("Lastname can not be null");
      }

      if (userDTO.getFirstName() == null) {
         throw new InvalidDtoException("Firstname can not be null");
      }

      if (!this.VALID_EMAIL_ADDRESS_REGEX.matcher(userDTO.getEmail()).matches()) {
         throw new InvalidDtoException("An invalid email pattern");
      }
   }
}
