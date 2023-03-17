package tiimae.webshop.iprwc.DAO;

import kong.unirest.json.JSONArray;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import tiimae.webshop.iprwc.DAO.repo.CartRepository;
import tiimae.webshop.iprwc.DTO.CartDTO;
import tiimae.webshop.iprwc.exception.EntryNotFoundException;
import tiimae.webshop.iprwc.mapper.CartMapper;
import tiimae.webshop.iprwc.models.Cart;
import tiimae.webshop.iprwc.models.Product;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class CartDAO {

    private CartMapper cartMapper;
    private CartRepository cartRepository;

    public void postCartItemAndReturnMap(CartDTO cartDTO, boolean detail) throws EntryNotFoundException {

        final Optional<Cart> cartByUserIdAndProductId = this.cartRepository.getCartByUserIdAndProductId(UUID.fromString(cartDTO.getUserId()), UUID.fromString(cartDTO.getProductId()));

        if (cartByUserIdAndProductId.isPresent()) {
            final Cart cart = cartByUserIdAndProductId.get();
            if (detail) {
                cart.setQuantity(cart.getQuantity() + cartDTO.getQuantity());
            } else {
                cart.setQuantity(cartDTO.getQuantity());
            }
            this.cartRepository.saveAndFlush(cart);
        } else {
            final Cart cartItem = this.cartMapper.createCartItem(cartDTO);
            this.cartRepository.save(cartItem);
        }
    }

    public List<Map<String, String>> getCart(UUID id) {
        return this.getFullCart(id);
    }

    @Transactional
    public void deleteFromCart(UUID userId, UUID productId) throws EntryNotFoundException {
        final Optional<Cart> cartByUserIdAndProductId = this.cartRepository.getCartByUserIdAndProductId(userId, productId);

        if (cartByUserIdAndProductId.isEmpty()) {
            throw new EntryNotFoundException("This item was not in your cart");
        }

        this.cartRepository.delete(cartByUserIdAndProductId.get());
    }

    @Transactional
    public void clearCart(UUID userId) {
        final List<Cart> cartByUserId = this.cartRepository.getCartByUserId(userId);

        for (Cart item : cartByUserId) {
            item.setUser(null);
            item.setProduct(null);
            this.cartRepository.delete(item);
        }
    }

    public List<Map<String, String>> getFullCart(UUID id) {
        final List<Cart> cartByUserId = this.cartRepository.getCartByUserId(id);

        return cartByUserId.stream().map(cart -> {
            final HashMap<String, String> map = new HashMap<>();
            map.put("productId", String.valueOf(cart.getProduct().getId()));
            map.put("quantity", String.valueOf(cart.getQuantity()));
            return map;
        }).collect(Collectors.toList());
    }





}
