package tiimae.webshop.iprwc.validators.auth;

import java.util.Optional;

import org.springframework.stereotype.Component;

import tiimae.webshop.iprwc.DAO.ProductDAO;
import tiimae.webshop.iprwc.DAO.UserDAO;
import tiimae.webshop.iprwc.DTO.LoginDTO;
import tiimae.webshop.iprwc.exception.EntryNotFoundException;
import tiimae.webshop.iprwc.exception.InvalidDtoException;
import tiimae.webshop.iprwc.models.User;
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
    }

}
