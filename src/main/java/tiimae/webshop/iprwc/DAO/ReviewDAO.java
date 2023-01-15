package tiimae.webshop.iprwc.DAO;

import org.springframework.stereotype.Component;
import tiimae.webshop.iprwc.DAO.repo.ReviewRepository;
import tiimae.webshop.iprwc.DTO.ReviewDTO;
import tiimae.webshop.iprwc.mapper.ReviewMapper;
import tiimae.webshop.iprwc.models.Review;

@Component
public class ReviewDAO {

    private ReviewRepository reviewRepository;
    private ReviewMapper reviewMapper;

    public ReviewDAO(ReviewRepository reviewRepository, ReviewMapper reviewMapper) {
        this.reviewRepository = reviewRepository;
        this.reviewMapper = reviewMapper;
    }

    public Review create(ReviewDTO reviewDTO) {
        final Review review = this.reviewMapper.toReview(reviewDTO);
        return this.reviewRepository.save(review);
    }
}
