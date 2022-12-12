package tiimae.webshop.iprwc.DAO.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import tiimae.webshop.iprwc.models.Supplier;

import java.util.UUID;

public interface SupplierRepository extends JpaRepository<Supplier, UUID> {
}
