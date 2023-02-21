package tiimae.webshop.iprwc.DAO.repo;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tiimae.webshop.iprwc.models.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {

    Optional<Product> findByProductName(String productName);

}
