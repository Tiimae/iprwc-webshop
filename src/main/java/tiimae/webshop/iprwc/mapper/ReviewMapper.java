package tiimae.webshop.iprwc.mapper;

import org.springframework.stereotype.Component;
import tiimae.webshop.iprwc.DAO.ProductDAO;
import tiimae.webshop.iprwc.DTO.ReviewDTO;
import tiimae.webshop.iprwc.exception.EntryNotFoundException;
import tiimae.webshop.iprwc.models.Product;
import tiimae.webshop.iprwc.models.Review;

import java.util.UUID;

@Component
public class ReviewMapper {

    private final ProductDAO productDAO;

    public ReviewMapper(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    public Review toReview(ReviewDTO reviewDTO) throws EntryNotFoundException {
        return new Review((long) reviewDTO.getStars(), reviewDTO.getDescription(), this.getProduct(UUID.fromString(reviewDTO.getProductId())));
    }

    public Product getProduct(UUID id) throws EntryNotFoundException {
        Product product = null;

        if (id != null) {
            product = this.productDAO.get(id);
        }
        return product;

    }
}
