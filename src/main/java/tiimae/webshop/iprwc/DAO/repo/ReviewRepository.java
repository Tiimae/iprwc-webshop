package tiimae.webshop.iprwc.DAO.repo;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tiimae.webshop.iprwc.models.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, UUID> {
}
