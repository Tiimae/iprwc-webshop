package tiimae.webshop.iprwc.validators;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;
import tiimae.webshop.iprwc.DAO.ProductDAO;
import tiimae.webshop.iprwc.DAO.UserAddressDAO;
import tiimae.webshop.iprwc.DAO.UserDAO;
import tiimae.webshop.iprwc.DTO.OrderDTO;
import tiimae.webshop.iprwc.models.User;
import tiimae.webshop.iprwc.models.UserAddress;


@Component
public class OrderValidator extends Validator {

   private UserAddressDAO userAddressDAO;
   private UserDAO userDAO;

   public OrderValidator(ProductDAO productDAO, UserAddressDAO userAddressDAO, UserDAO userDAO) {
      super(productDAO);
      this.userAddressDAO = userAddressDAO;
      this.userDAO = userDAO;
   }

   

   public String validateDTO(OrderDTO orderDTO) {
      for (String addressId : orderDTO.getAddressIds()) {
         String checkIfStringIsUUID = this.checkIfStringIsUUID(addressId);

         if (checkIfStringIsUUID != null) {
            return checkIfStringIsUUID;
         }

         Optional<UserAddress> optional = this.userAddressDAO.get(UUID.fromString(addressId));
      
         if (optional.isEmpty()) {
            return "The Address with the Id " + addressId + " doesn't exist!";
         }

         UserAddress userAddress = optional.get();

         UUID userId = userAddress.getUser().getId();

         if (!userId.equals(UUID.fromString(orderDTO.getUserId()))) {
            return "The user does not match with this address.";
         }
      }

      String checkIfStringIsUUID = this.checkIfStringIsUUID(orderDTO.getUserId());

         if (checkIfStringIsUUID != null) {
            return checkIfStringIsUUID;
         }

      Optional<User> user = this.userDAO.getUser(UUID.fromString(orderDTO.getUserId()));

      if (user.isEmpty()) {
         return "The user with the id " + orderDTO.getUserId() + " doesn't exsist";
      }
      
      return null;
   }

   public String validateOrderProducts(JSONArray products) {

      for (int i = 0; i < products.length(); i++) {
         JSONObject object = products.getJSONObject(i);  
         
         if (object.getLong("amount") < 1) {
            return "You can't order a product lower than 1 time";
         }

         String checkIfStringIsUUID = this.checkIfStringIsUUID(object.getString("id"));

         if (checkIfStringIsUUID != null) {
            return checkIfStringIsUUID;
         }
         
         String checkIfProductExists = this.CheckIfProductExists(UUID.fromString(object.getString("id")));

         if (checkIfProductExists != null) {
            return checkIfProductExists;
         }
      }


      return null;
   }

}
