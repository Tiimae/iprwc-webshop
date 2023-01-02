package tiimae.webshop.iprwc.DAO;

import org.springframework.stereotype.Component;
import tiimae.webshop.iprwc.DAO.repo.OrderRepository;
import tiimae.webshop.iprwc.DTO.OrderDTO;
import tiimae.webshop.iprwc.exception.EntryNotFoundException;
import tiimae.webshop.iprwc.mapper.OrderMapper;
import tiimae.webshop.iprwc.models.Order;

@Component
public class OrderDAO {

    private OrderRepository orderRepository;
    private OrderMapper orderMapper;

    public OrderDAO(OrderRepository orderRepository, OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
    }

    public Order create(OrderDTO orderDTO) throws EntryNotFoundException {
        return this.orderRepository.save(this.orderMapper.toOrder(orderDTO));
    }
}
