package tiimae.webshop.iprwc.validators;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

import tiimae.webshop.iprwc.DAO.ProductDAO;
import tiimae.webshop.iprwc.DAO.RoleDAO;
import tiimae.webshop.iprwc.DAO.UserDAO;
import tiimae.webshop.iprwc.DTO.UserDTO;
import tiimae.webshop.iprwc.models.Role;
import tiimae.webshop.iprwc.models.User;

@Component
public class UserValidator extends Validator {

   public void validateDTO(UUID id, UserDTO userDTO) {

   }

   public void validateId(String userId) {

   }
}
