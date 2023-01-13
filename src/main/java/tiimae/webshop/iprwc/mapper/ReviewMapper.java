package tiimae.webshop.iprwc.mapper;

import org.springframework.stereotype.Component;
import tiimae.webshop.iprwc.DAO.ProductDAO;
import tiimae.webshop.iprwc.DAO.repo.ProductRepository;
import tiimae.webshop.iprwc.DTO.ReviewDTO;
import tiimae.webshop.iprwc.models.Product;
import tiimae.webshop.iprwc.models.Review;

import java.util.UUID;

@Component
public class ReviewMapper {

    private ProductDAO productDAO;

    public ReviewMapper(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    public Review toReview(ReviewDTO reviewDTO) {
        return new Review((long) reviewDTO.getStars(), reviewDTO.getDescription(), this.getProduct(reviewDTO.getProductId()));
    }

    public Product getProduct(UUID id) {
        Product product = null;
        if (id != null) {
            product = this.productDAO.get(id);
        }
        return product;

    }
}
