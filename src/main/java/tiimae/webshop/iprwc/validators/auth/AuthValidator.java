package tiimae.webshop.iprwc.validators.auth;

import java.util.Optional;

import org.springframework.stereotype.Component;

import tiimae.webshop.iprwc.DAO.ProductDAO;
import tiimae.webshop.iprwc.DAO.UserDAO;
import tiimae.webshop.iprwc.DTO.LoginDTO;
import tiimae.webshop.iprwc.DTO.UserDTO;
import tiimae.webshop.iprwc.exception.EntryNotFoundException;
import tiimae.webshop.iprwc.exception.InvalidDtoException;
import tiimae.webshop.iprwc.models.User;
import tiimae.webshop.iprwc.validators.UserValidator;
import tiimae.webshop.iprwc.validators.Validator;

@Component
public class AuthValidator extends Validator {

    public void loginValidation(LoginDTO loginDTO) throws InvalidDtoException {
        if (loginDTO.getPassword() == null) {
            throw new InvalidDtoException("A password needs to be specified");
        }

        if (loginDTO.getEmail() == null) {
            throw new InvalidDtoException("A email need te be specified");
        }

        if (!this.VALID_EMAIL_ADDRESS_REGEX.matcher(loginDTO.getEmail()).matches()) {
            throw new InvalidDtoException("An invalid email pattern");
        }
    }

    public void registerValidation(UserDTO userDTO) throws InvalidDtoException {
        if (userDTO.getPassword() == null) {
            throw new InvalidDtoException("A password needs to be specified");
        }

        if (userDTO.getEmail() == null) {
            throw new InvalidDtoException("A email need te be specified");
        }

        if (userDTO.getFirstName() == null) {
            throw new InvalidDtoException("A firstname need te be specified");
        }

        if (userDTO.getLastName() == null) {
            throw new InvalidDtoException("A lastname need te be specified");
        }

        if (!this.VALID_EMAIL_ADDRESS_REGEX.matcher(userDTO.getEmail()).matches()) {
            throw new InvalidDtoException("An invalid email pattern");
        }

        if (!this.VALID_PASSWORD_REGEX.matcher(userDTO.getPassword()).matches()) {
            throw new InvalidDtoException("Password needs at least 8 characters, 1 numbers, 1 lowercase letter, 1 uppercase characters and 1 special characters!");
        }
    }

    public void resetPasswordValidation(UserDTO userDTO) throws InvalidDtoException {
        if (userDTO.getPassword() == null) {
            throw new InvalidDtoException("A password needs to be specified");
        }

        if (userDTO.getEmail() == null) {
            throw new InvalidDtoException("A email need te be specified");
        }

        if (!this.VALID_EMAIL_ADDRESS_REGEX.matcher(userDTO.getEmail()).matches()) {
            throw new InvalidDtoException("An invalid email pattern");
        }

        if (!this.VALID_PASSWORD_REGEX.matcher(userDTO.getPassword()).matches()) {
            throw new InvalidDtoException("Password needs at least 8 characters, 1 numbers, 1 lowercase letter, 1 uppercase characters and 1 special characters!");
        }
    }

}
