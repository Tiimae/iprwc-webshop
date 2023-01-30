package tiimae.webshop.iprwc.DAO.repo;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tiimae.webshop.iprwc.models.ProductImage;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, UUID> {
    List<ProductImage> findAllByProductId(UUID product_id);
}
