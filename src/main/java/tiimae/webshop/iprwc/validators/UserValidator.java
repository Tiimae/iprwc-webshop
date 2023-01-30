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
   private UserDAO userDAO;
   private RoleDAO roleDAO;

   public UserValidator(ProductDAO productDAO, UserDAO userDAO, RoleDAO roleDAO) {
      super(productDAO);
      this.userDAO = userDAO;
      this.roleDAO = roleDAO;
   }

   public String validateDTO(UserDTO userDTO) {

      Optional<User> byEmail = this.userDAO.getByEmail(userDTO.getEmail());
      
      if (!byEmail.isEmpty()) {
         return "Something went wrong while updating the user";
      }

      if (userDTO.getRoleIds().length > 0) {
         for (String roleId : userDTO.getRoleIds()) {
            String checkIfStringIsUUID = this.checkIfStringIsUUID(roleId);
            
            if (checkIfStringIsUUID != null) {
               return checkIfStringIsUUID;
            }

            Optional<Role> role = this.roleDAO.getRole(UUID.fromString(roleId));
         
            if (role.isEmpty()) {
               return "De rol met de id: " + roleId + " bestaat niet!";
            }
         }
      }

      return null;
   }

   public String validateId(String userId) {
      String checkIfStringIsUUID = this.checkIfStringIsUUID(userId);

      if (checkIfStringIsUUID != null) { 
          return checkIfStringIsUUID;
      }

      final Optional<User> byName = this.userDAO.getUser(UUID.fromString(userId));

      if (byName == null) {
          return "The user with id: " + userId + " doesn't exist";
      }

      return null;
   }
}
