package tiimae.webshop.iprwc.DAO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import tiimae.webshop.iprwc.DAO.repo.ProductRepository;
import tiimae.webshop.iprwc.DTO.ProductDTO;
import tiimae.webshop.iprwc.mapper.ProductMapper;
import tiimae.webshop.iprwc.models.Product;

@Component
@AllArgsConstructor
public class ProductDAO {

    private ProductRepository productRepository;

    private ProductMapper productMapper;

    public Product get(UUID productId) {
        return this.productRepository.findById(productId).get();
    }

    public List<Product> getAll() {
        return this.productRepository.findAll();
    }

    public Product post(ProductDTO productDTO) {
        return this.productRepository.save(this.productMapper.toProduct(productDTO));
    }

    public Product update(UUID productId, ProductDTO productDTO) {
        final Optional<Product> byId = this.productRepository.findById(productId);

        if (byId.isEmpty()) {
            return null;
        }

        final Product product = this.productMapper.mergeProduct(byId.get(), productDTO);

        return this.productRepository.saveAndFlush(product);
    }

    public Product delete(UUID productId) {

        final Optional<Product> byId = this.productRepository.findById(productId);

        if (byId.isEmpty()) {
            return null;
        }

        final Product product = byId.get();

        product.setDeleted(true);

        return this.productRepository.saveAndFlush(product);

    }

    public Product restore(UUID productId) {

        final Optional<Product> byId = this.productRepository.findById(productId);

        if (byId.isEmpty()) {
            return null;
        }

        final Product product = byId.get();

        product.setDeleted(false);

        return this.productRepository.saveAndFlush(product);

    }
}
