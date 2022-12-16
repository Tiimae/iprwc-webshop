package tiimae.webshop.iprwc.DAO;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import tiimae.webshop.iprwc.DAO.repo.ProductImageRepository;
import tiimae.webshop.iprwc.mapper.ProductImageMapper;
import tiimae.webshop.iprwc.models.Product;
import tiimae.webshop.iprwc.models.ProductImage;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

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

    public void update(String[] deleteImage, MultipartFile[] newImage, Product product) throws IOException {
        final List<ProductImage> allByProduct = this.productImageRepository.findAllByProductId(product.getId());

        for (ProductImage image : allByProduct) {
            for (String imageString : deleteImage) {
                if (Objects.equals(image.getImagePath(), imageString)) {
                    final String name = image.getImagePath().split("/")[image.getImagePath().split("/").length - 1];

                    this.imageDAO.deleteImage(name, "product");
                    this.productImageRepository.delete(image);
                }
            }
        }

        for (MultipartFile multipartFile : newImage) {
            this.create(multipartFile, product);
        }
    }

    public void delete(UUID productId, Product product) throws IOException {
        final List<ProductImage> allByProduct = this.productImageRepository.findAllByProductId(productId);

        for (ProductImage productImage : allByProduct) {
            final String name = productImage.getImagePath().split("/")[productImage.getImagePath().split("/").length - 1];

            this.imageDAO.deleteImage(name, "product");
            this.productImageRepository.delete(productImage);
        }
    }
}
