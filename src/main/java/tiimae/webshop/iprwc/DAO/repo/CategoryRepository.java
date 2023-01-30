package tiimae.webshop.iprwc.DAO.repo;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tiimae.webshop.iprwc.models.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {
    Optional<Category> findByCategoryName(String categoryName);
}
