package tiimae.webshop.iprwc.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import tiimae.webshop.iprwc.DAO.ReviewDAO;
import tiimae.webshop.iprwc.DTO.ReviewDTO;
import tiimae.webshop.iprwc.constants.ApiConstant;
import tiimae.webshop.iprwc.service.ApiResponseService;

@RestController
public class ReviewController {

    private ReviewDAO reviewDAO;

    public ReviewController(ReviewDAO reviewDAO) {
        this.reviewDAO = reviewDAO;
    }

    @PostMapping(value = ApiConstant.getAllReview)
    @ResponseBody
    public ApiResponseService post(@RequestBody ReviewDTO reviewDTO) {
        return new ApiResponseService(HttpStatus.CREATED, this.reviewDAO.create(reviewDTO));
    }

}
