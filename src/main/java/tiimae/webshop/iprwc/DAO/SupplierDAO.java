package tiimae.webshop.iprwc.DAO;

import org.springframework.stereotype.Component;
import tiimae.webshop.iprwc.DAO.repo.SupplierRepository;
import tiimae.webshop.iprwc.DTO.SupplierDTO;
import tiimae.webshop.iprwc.mapper.SupplierMapper;
import tiimae.webshop.iprwc.models.Product;
import tiimae.webshop.iprwc.models.Supplier;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class SupplierDAO {

    private SupplierRepository supplierRepository;

    private SupplierMapper supplierMapper;

    public SupplierDAO(SupplierRepository supplierRepository, SupplierMapper supplierMapper) {
        this.supplierRepository = supplierRepository;
        this.supplierMapper = supplierMapper;
    }

    public Supplier get(UUID supplierId) {
        return this.supplierRepository.findById(supplierId).get();
    }

    public List<Supplier> getAll() {
        return this.supplierRepository.findAll();
    }

    public Supplier create(SupplierDTO supplierDTO) {
        return this.supplierRepository.save(this.supplierMapper.toSupplier(supplierDTO));
    }

    public Supplier put(UUID supplierId, SupplierDTO supplierDTO) {
        final Optional<Supplier> byId = this.supplierRepository.findById(supplierId);

        if (byId.isEmpty()) {
            return null;
        }

        final Supplier supplier = this.supplierMapper.mergeSupplier(byId.get(), supplierDTO);

        return this.supplierRepository.saveAndFlush(supplier);
    }

    public void delete(UUID supplierId) {
        final Optional<Supplier> byId = this.supplierRepository.findById(supplierId);

        if (byId.isEmpty()) {
            return;
        }

        for (Product product : byId.get().getProducts()) {
            product.setSupplier(null);
        }

        byId.get().getProducts().clear();
        this.supplierRepository.delete(byId.get());
    }
}
