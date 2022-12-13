package tiimae.webshop.iprwc.DAO;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import tiimae.webshop.iprwc.DAO.repo.ProductImageRepository;
import tiimae.webshop.iprwc.mapper.ProductImageMapper;
import tiimae.webshop.iprwc.models.Product;
import tiimae.webshop.iprwc.models.ProductImage;

import java.io.IOException;

@Component
public class ProductImageDAO {

    private ProductImageRepository productImageRepository;
    private ProductImageMapper productImageMapper;
    private ImageDAO imageDAO;

    public ProductImageDAO(ProductImageRepository productImageRepository, ProductImageMapper productImageMapper, ImageDAO imageDAO) {
        this.productImageRepository = productImageRepository;
        this.productImageMapper = productImageMapper;
        this.imageDAO = imageDAO;
    }

    public void create(MultipartFile file, Product product) throws IOException {

        final String path = this.imageDAO.saveBrandImage(file, product.getProductName(), "product");

        this.productImageRepository.save(this.productImageMapper.toProductImage(path, product));

    }
}
