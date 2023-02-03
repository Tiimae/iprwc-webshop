package tiimae.webshop.iprwc.mapper;

import org.springframework.stereotype.Component;

import tiimae.webshop.iprwc.models.Product;
import tiimae.webshop.iprwc.models.ProductImage;

@Component
public class ProductImageMapper {

    public ProductImage toProductImage(String path, Product product) {
        return new ProductImage(path, product);
    }

}
