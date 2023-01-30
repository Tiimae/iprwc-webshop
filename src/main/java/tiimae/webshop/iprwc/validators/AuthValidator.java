package tiimae.webshop.iprwc.validators;

import java.util.Optional;

import org.springframework.stereotype.Component;

import tiimae.webshop.iprwc.DAO.ProductDAO;
import tiimae.webshop.iprwc.DAO.UserDAO;
import tiimae.webshop.iprwc.DTO.LoginDTO;
import tiimae.webshop.iprwc.models.User;

@Component
public class AuthValidator extends Validator {

   private UserDAO userDAO;

   public AuthValidator(ProductDAO productDAO, UserDAO userDAO) {
      super(productDAO);
      this.userDAO = userDAO;
   }

   public String registerValidation(String email, String password) {
   
      if (!this.VALID_EMAIL_ADDRESS_REGEX.matcher(email).matches()) {
         return "The email " + email + "Is not a valid email address";
      }

      Optional<User> byEmail = this.userDAO.getByEmail(email);

      if (!byEmail.isEmpty()) {
         return "Something went wrong!";
      }

      if(password == null) {
         return "Password can't be empty!";
      }

      if (password.length() < 8) {
         return "password must be 8 characters";
      }

      return null;
   }

   public String loginValidation(LoginDTO loginDTO) {
      return null;
   }
   
}
