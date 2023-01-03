package tiimae.webshop.iprwc.mapper;

import org.springframework.stereotype.Component;
import tiimae.webshop.iprwc.DAO.OrderProductDAO;
import tiimae.webshop.iprwc.DAO.UserAddressDAO;
import tiimae.webshop.iprwc.DAO.UserDAO;
import tiimae.webshop.iprwc.DTO.OrderDTO;
import tiimae.webshop.iprwc.exception.EntryNotFoundException;
import tiimae.webshop.iprwc.models.*;

import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;

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
                this.getUser(orderDTO.getUserId()),
                this.getOrderProducts(orderDTO.getProductIds()),
                this.getUserAddresses(orderDTO.getAddressIds())
        );
    }

    public Set<UserAddress> getUserAddresses(UUID[] addressId) {
        Set<UserAddress> userAddresses = new HashSet<>();
        if (addressId != null) {
            userAddresses = Arrays.stream(addressId)
                    .map(id -> this.userAddressDAO.get(id).orElse(null))
                    .collect(Collectors.toSet());
        }

        return userAddresses;
    }

    public Set<OrderProduct> getOrderProducts(UUID[] orderId) {
        Set<OrderProduct> orderProducts = new HashSet<>();
        if (orderId != null) {
            orderProducts = Arrays.stream(orderId)
                    .map(id -> this.orderProductDAO.get(id).orElse(null))
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
