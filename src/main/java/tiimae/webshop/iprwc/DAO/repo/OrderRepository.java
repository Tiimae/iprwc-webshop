package tiimae.webshop.iprwc.DAO.repo;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import tiimae.webshop.iprwc.models.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {

    List<Order> getOrdersByUserId(UUID userId);

    @Transactional
    @Modifying
    @Query("delete from Order t where t.id = ?1")
    void deleteById(UUID id);
}
