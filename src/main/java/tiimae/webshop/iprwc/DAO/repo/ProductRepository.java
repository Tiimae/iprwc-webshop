package tiimae.webshop.iprwc.DAO.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import tiimae.webshop.iprwc.models.Product;

import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
}
