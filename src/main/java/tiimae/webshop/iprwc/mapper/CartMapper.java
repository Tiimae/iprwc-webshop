package tiimae.webshop.iprwc.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import tiimae.webshop.iprwc.DAO.ProductDAO;
import tiimae.webshop.iprwc.DAO.UserDAO;
import tiimae.webshop.iprwc.DTO.CartDTO;
import tiimae.webshop.iprwc.exception.EntryNotFoundException;
import tiimae.webshop.iprwc.models.Cart;
import tiimae.webshop.iprwc.models.Product;
import tiimae.webshop.iprwc.models.User;

import java.util.UUID;

@Component
@AllArgsConstructor
public class CartMapper {

    private ProductDAO productDAO;
    private UserDAO userDAO;

    public Cart createCartItem(CartDTO cartDTO) throws EntryNotFoundException {
        final Cart cart = new Cart();
        cart.setProduct(this.getProduct(UUID.fromString(cartDTO.getProductId())));
        cart.setUser(this.getUser(UUID.fromString(cartDTO.getUserId())));
        cart.setQuantity(cartDTO.getQuantity());

        return cart;
    }

    public Product getProduct(UUID id) throws EntryNotFoundException {
        Product product = null;
        if (id != null) {
            product = this.productDAO.get(id);
        }
        return product;

    }

    private User getUser(UUID userId) throws EntryNotFoundException {
        User user = null;

        if (userId != null) {
            user = this.userDAO.getUser(userId);
        }

        return user;
    }

}
