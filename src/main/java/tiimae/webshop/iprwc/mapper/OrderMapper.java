package tiimae.webshop.iprwc.mapper;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import tiimae.webshop.iprwc.DAO.OrderProductDAO;
import tiimae.webshop.iprwc.DAO.UserAddressDAO;
import tiimae.webshop.iprwc.DAO.UserDAO;
import tiimae.webshop.iprwc.DTO.OrderDTO;
import tiimae.webshop.iprwc.exception.EntryNotFoundException;
import tiimae.webshop.iprwc.models.Order;
import tiimae.webshop.iprwc.models.OrderProduct;
import tiimae.webshop.iprwc.models.User;
import tiimae.webshop.iprwc.models.UserAddress;

@Component
public class OrderMapper {

    private UserAddressDAO userAddressDAO;
    private OrderProductDAO orderProductDAO;
    private UserDAO userDAO;

    public OrderMapper(UserAddressDAO userAddressDAO, OrderProductDAO orderProductDAO, UserDAO userDAO) {
        this.userAddressDAO = userAddressDAO;
        this.orderProductDAO = orderProductDAO;
        this.userDAO = userDAO;
    }

    public Order toOrder(OrderDTO orderDTO) throws EntryNotFoundException {
        return new Order(
                orderDTO.getOrderId(),
                orderDTO.getOrderDate(),
                this.getUser(UUID.fromString(orderDTO.getUserId())),
                this.getOrderProducts(orderDTO.getProductIds()),
                this.getUserAddresses(orderDTO.getAddressIds())
        );
    }

    public Set<UserAddress> getUserAddresses(String[] addressId) {
        Set<UserAddress> userAddresses = new HashSet<>();
        if (addressId != null) {
            userAddresses = Arrays.stream(addressId)
                    .map(id -> this.userAddressDAO.get(UUID.fromString(id)).orElse(null))
                    .collect(Collectors.toSet());
        }

        return userAddresses;
    }

    public Set<OrderProduct> getOrderProducts(String[] orderId) {
        Set<OrderProduct> orderProducts = new HashSet<>();
        if (orderId != null) {
            orderProducts = Arrays.stream((orderId))
                    .map(id -> this.orderProductDAO.get(UUID.fromString(id)).orElse(null))
                    .collect(Collectors.toSet());
        }

        return orderProducts;
    }

    public User getUser(UUID id) throws EntryNotFoundException {
        User user = null;

        if (id != null) {
            final Optional<User> user1 = this.userDAO.getUser(id);

            if (user1.isEmpty()) {
                throw new EntryNotFoundException("User has not been found!");
            }

            user = user1.get();
        }

        return user;
    }

}
