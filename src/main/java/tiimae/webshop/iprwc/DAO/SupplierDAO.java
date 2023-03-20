package tiimae.webshop.iprwc.DAO;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import tiimae.webshop.iprwc.DAO.repo.SupplierRepository;
import tiimae.webshop.iprwc.DTO.SupplierDTO;
import tiimae.webshop.iprwc.exception.EntryAlreadyExistsException;
import tiimae.webshop.iprwc.exception.EntryNotFoundException;
import tiimae.webshop.iprwc.mapper.SupplierMapper;
import tiimae.webshop.iprwc.models.Product;
import tiimae.webshop.iprwc.models.Supplier;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@AllArgsConstructor
public class SupplierDAO {

    private SupplierRepository supplierRepository;

    private SupplierMapper supplierMapper;

    public Supplier get(UUID supplierId) throws EntryNotFoundException {
        final Optional<Supplier> byId = this.supplierRepository.findById(supplierId);
        this.checkIfExists(byId);
        return byId.get();
    }

    public List<Supplier> getAll() {
        return this.supplierRepository.findAll();
    }

    public Supplier create(SupplierDTO supplierDTO) throws EntryAlreadyExistsException {
        this.checkIfSupplierNameExists(supplierDTO.getName(), null);
        return this.supplierRepository.save(this.supplierMapper.toSupplier(supplierDTO));
    }

    @Transactional
    public Supplier put(UUID supplierId, SupplierDTO supplierDTO) throws EntryNotFoundException, EntryAlreadyExistsException {
        final Optional<Supplier> byId = this.supplierRepository.findById(supplierId);
        this.checkIfExists(byId);
        this.checkIfSupplierNameExists(supplierDTO.getName(), supplierId);

        final Supplier supplier = this.supplierMapper.mergeSupplier(byId.get(), supplierDTO);

        return this.supplierRepository.saveAndFlush(supplier);
    }

    @Transactional
    public void delete(UUID supplierId) throws EntryNotFoundException {
        final Optional<Supplier> byId = this.supplierRepository.findById(supplierId);
        this.checkIfExists(byId);

        for (Product product : byId.get().getProducts()) {
            product.setSupplier(null);
        }

        byId.get().getProducts().clear();
        this.supplierRepository.delete(byId.get());
    }

    public void checkIfExists(Optional<Supplier> supplier) throws EntryNotFoundException {
        if (supplier.isEmpty()) {
            throw new EntryNotFoundException("This product has not been found!");
        }
    }

    public void checkIfSupplierNameExists(String supplierName, UUID id) throws EntryAlreadyExistsException {
        Optional<Supplier> supplierByName = this.supplierRepository.findByName(supplierName);

        if (supplierByName.isPresent()) {
            if (id != null) {
                if (!supplierByName.get().getId().equals(id)) {
                    throw new EntryAlreadyExistsException("Supplier name already exists");
                }
            } else {

                throw new EntryAlreadyExistsException("Supplier name already exists");
            }
        }
    }
}
