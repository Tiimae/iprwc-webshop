package tiimae.webshop.iprwc.DAO;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import tiimae.webshop.iprwc.DAO.repo.ReviewRepository;
import tiimae.webshop.iprwc.DTO.ReviewDTO;
import tiimae.webshop.iprwc.exception.EntryNotFoundException;
import tiimae.webshop.iprwc.mapper.ReviewMapper;
import tiimae.webshop.iprwc.models.Review;

@Component
@AllArgsConstructor
public class ReviewDAO {

    private ReviewRepository reviewRepository;
    private ReviewMapper reviewMapper;

    public Review create(ReviewDTO reviewDTO) throws EntryNotFoundException {
        final Review review = this.reviewMapper.toReview(reviewDTO);
        return this.reviewRepository.save(review);
    }
}
