package tiimae.webshop.iprwc.DAO.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import tiimae.webshop.iprwc.models.Order;

import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
}
