package tiimae.webshop.iprwc.DAO;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import tiimae.webshop.iprwc.DAO.repo.OrderProductRepository;
import tiimae.webshop.iprwc.DAO.repo.OrderRepository;
import tiimae.webshop.iprwc.DTO.OrderDTO;
import tiimae.webshop.iprwc.exception.EntryNotFoundException;
import tiimae.webshop.iprwc.mapper.OrderMapper;
import tiimae.webshop.iprwc.models.Order;

@Component
@AllArgsConstructor
public class OrderDAO {

    private OrderRepository orderRepository;
    private OrderMapper orderMapper;
    private OrderProductRepository orderProductRepository;

    public List<Order> getByUserId(UUID userId) {
        return this.orderRepository.getOrdersByUserId(userId);
    }

    public Order create(OrderDTO orderDTO) throws EntryNotFoundException {
        return this.orderRepository.save(this.orderMapper.toOrder(orderDTO));
    }
}
