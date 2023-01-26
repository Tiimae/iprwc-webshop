package tiimae.webshop.iprwc.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import tiimae.webshop.iprwc.DAO.ReviewDAO;
import tiimae.webshop.iprwc.DTO.ReviewDTO;
import tiimae.webshop.iprwc.constants.ApiConstant;
import tiimae.webshop.iprwc.service.ApiResponseService;
import tiimae.webshop.iprwc.validators.ReviewValidator;

@RestController
@AllArgsConstructor
public class ReviewController {

    private ReviewDAO reviewDAO;
    private ReviewValidator reviewValidator;

    @PostMapping(value = ApiConstant.getAllReview)
    @ResponseBody
    public ApiResponseService post(@RequestBody ReviewDTO reviewDTO) {
        String validateDTO = this.reviewValidator.validateDTO(reviewDTO);

        if (validateDTO != null) {
            return new ApiResponseService(HttpStatus.BAD_REQUEST, validateDTO);
        }

        return new ApiResponseService(HttpStatus.CREATED, this.reviewDAO.create(reviewDTO));
    }

}
