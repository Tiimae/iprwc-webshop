package tiimae.webshop.iprwc.DAO.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import tiimae.webshop.iprwc.models.Category;

import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {
}