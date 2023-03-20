package tiimae.webshop.iprwc.DAO;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import tiimae.webshop.iprwc.DAO.repo.ProductRepository;
import tiimae.webshop.iprwc.DTO.ProductDTO;
import tiimae.webshop.iprwc.exception.EntryAlreadyExistsException;
import tiimae.webshop.iprwc.exception.EntryNotFoundException;
import tiimae.webshop.iprwc.mapper.ProductMapper;
import tiimae.webshop.iprwc.models.Product;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@AllArgsConstructor
public class ProductDAO {

    private ProductRepository productRepository;

    private ProductMapper productMapper;

    public Product get(UUID productId) throws EntryNotFoundException {
        final Optional<Product> byId = this.productRepository.findById(productId);
        this.checkIfExists(byId);

        return byId.get();
    }

    public List<Product> getAll() {
        return this.productRepository.findAll();
    }

    public Product post(ProductDTO productDTO) throws EntryNotFoundException, EntryAlreadyExistsException {
        this.checkIfProductNameExists(productDTO.getName(), null);
        return this.productRepository.save(this.productMapper.toProduct(productDTO));
    }

    @Transactional
    public Product update(UUID productId, ProductDTO productDTO) throws EntryNotFoundException, EntryAlreadyExistsException {
        final Optional<Product> byId = this.productRepository.findById(productId);
        this.checkIfExists(byId);
        this.checkIfProductNameExists(productDTO.getName(), productId);

        final Product product = this.productMapper.mergeProduct(byId.get(), productDTO);

        return this.productRepository.saveAndFlush(product);
    }

    public Product delete(UUID productId) throws EntryNotFoundException {

        final Optional<Product> byId = this.productRepository.findById(productId);
        this.checkIfExists(byId);

        final Product product = byId.get();

        product.setDeleted(true);

        return this.productRepository.saveAndFlush(product);

    }

    public Product restore(UUID productId) throws EntryNotFoundException {

        final Optional<Product> byId = this.productRepository.findById(productId);
        this.checkIfExists(byId);

        final Product product = byId.get();

        product.setDeleted(false);

        return this.productRepository.saveAndFlush(product);

    }

    public void checkIfExists(Optional<Product> product) throws EntryNotFoundException {
        if (product.isEmpty()) {
            throw new EntryNotFoundException("This product has not been found!");
        }
    }

    public void checkIfProductNameExists(String productName, UUID id) throws EntryAlreadyExistsException {
        Optional<Product> productByName = this.productRepository.findByProductName(productName);

        if (productByName.isPresent()) {
            if (id != null) {
                if (!productByName.get().getId().equals(id)) {
                    throw new EntryAlreadyExistsException("Product name already exists");
                }
            } else {

                throw new EntryAlreadyExistsException("Product name already exists");
            }
        }
    }
}
