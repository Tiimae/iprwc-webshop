package tiimae.webshop.iprwc.mapper;

import org.springframework.stereotype.Component;
import tiimae.webshop.iprwc.DAO.BrandDAO;
import tiimae.webshop.iprwc.DAO.CategoryDAO;
import tiimae.webshop.iprwc.DAO.SupplierDAO;
import tiimae.webshop.iprwc.DTO.ProductDTO;
import tiimae.webshop.iprwc.models.Brand;
import tiimae.webshop.iprwc.models.Category;
import tiimae.webshop.iprwc.models.Product;
import tiimae.webshop.iprwc.models.Supplier;

import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;

@Component
public class ProductMapper {

    private BrandDAO brandDAO;
    private CategoryDAO categoryDAO;
    private SupplierDAO supplierDAO;

    public ProductMapper(BrandDAO brandDAO, CategoryDAO categoryDAO, SupplierDAO supplierDAO) {
        this.brandDAO = brandDAO;
        this.categoryDAO = categoryDAO;
        this.supplierDAO = supplierDAO;
    }

    public Product toProduct(ProductDTO productDTO) {
        final Brand brand = this.getBrand(productDTO.getBrandId());
        final Category category = this.getCategory(productDTO.getCategoryId());
        final Supplier supplier = this.getSupplier(productDTO.getSupplierId());

        return new Product(productDTO.getName(), productDTO.getDescription(), productDTO.getPrice(), brand, category, supplier, new HashSet<>(), new HashSet<>(), new HashSet<>());
    }

    public Product mergeProduct(Product base, ProductDTO update) {
        base.setProductName(update.getName());
        base.setDescription(update.getDescription());
        base.setPrice(update.getPrice());

        if (update.getBrandId() != null) {
            base.setBrand(this.getBrand(update.getBrandId()));
        }

        if (update.getCategoryId() != null) {
            base.setCategory(this.getCategory(update.getCategoryId()));
        }

        if (update.getSupplierId() != null) {
            base.setSupplier(this.getSupplier(update.getSupplierId()));
        }

        return base;

    }

    public Brand getBrand(UUID brandId) {
        Brand brand = null;

        if (brandId != null) {
            brand = this.brandDAO.getBrand(brandId);
        }

        return brand;
    }

    public Category getCategory(UUID categoryId) {
        Category category = null;

        if (categoryId != null) {
           category = this.categoryDAO.get(categoryId);
        }

        return category;
    }

    public Supplier getSupplier(UUID supplierId) {
        Supplier supplier = null;

        if (supplierId != null) {
            supplier = this.supplierDAO.get(supplierId);
        }

        return supplier;
    }

}
