package tiimae.webshop.iprwc.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import tiimae.webshop.iprwc.DAO.CategoryDAO;
import tiimae.webshop.iprwc.DTO.CategoryDTO;
import tiimae.webshop.iprwc.constants.ApiConstant;
import tiimae.webshop.iprwc.constants.RoleEnum;
import tiimae.webshop.iprwc.mapper.CategoryMapper;
import tiimae.webshop.iprwc.models.Category;
import tiimae.webshop.iprwc.service.response.ApiResponseService;
import tiimae.webshop.iprwc.validators.CategoryValidator;

@RestController
@AllArgsConstructor
public class CategoryController {

    private CategoryDAO categoryDAO;
    private CategoryMapper categoryMapper;
    private CategoryValidator categoryValidator;

    @GetMapping(ApiConstant.getOneCategories)
    @ResponseBody
    public ApiResponseService get(@PathVariable String categoryId) {
        final String validateId = this.categoryValidator.validateId(categoryId);

        if (validateId != null) {
            return new ApiResponseService(HttpStatus.BAD_REQUEST, validateId);
        }

        return new ApiResponseService(HttpStatus.ACCEPTED, this.categoryDAO.get(UUID.fromString(categoryId)));
    }

    @GetMapping(ApiConstant.getAllCategories)
    @ResponseBody
    public ApiResponseService getAll() {
        return new ApiResponseService(HttpStatus.ACCEPTED, this.categoryDAO.allCategories());
    }

    @PostMapping(ApiConstant.getAllCategories)
    @ResponseBody
    @Secured(RoleEnum.Admin.CODENAME)
    public ApiResponseService post(@RequestBody CategoryDTO categoryDTO) {
        final String validate = this.categoryValidator.validateDTO(categoryDTO, null);

        if (validate != null) {
            return new ApiResponseService(HttpStatus.BAD_REQUEST, validate);
        }

        final Category category = this.categoryMapper.toCategory(categoryDTO);

        return new ApiResponseService(HttpStatus.ACCEPTED, this.categoryDAO.create(category));
    }

    @PutMapping(ApiConstant.getOneCategories)
    @ResponseBody
    @Secured(RoleEnum.Admin.CODENAME)
    public ApiResponseService put(@PathVariable String categoryId, @RequestBody CategoryDTO categoryDTO) {
        final String validateId = this.categoryValidator.validateId(categoryId);

        if (validateId != null) {
            return new ApiResponseService(HttpStatus.BAD_REQUEST, validateId);
        }

        final String validate = this.categoryValidator.validateDTO(categoryDTO, UUID.fromString(categoryId));

        if (validate != null) {
            return new ApiResponseService(HttpStatus.BAD_REQUEST, validate);
        }

        return new ApiResponseService(HttpStatus.ACCEPTED, this.categoryDAO.update(UUID.fromString(categoryId), categoryDTO));
    }

    @DeleteMapping(ApiConstant.getOneCategories)
    @ResponseBody
    @Secured(RoleEnum.Admin.CODENAME)
    public ApiResponseService delete(@PathVariable String categoryId) {
        final String validateId = this.categoryValidator.validateId(categoryId);

        if (validateId != null) {
            return new ApiResponseService(HttpStatus.BAD_REQUEST, validateId);
        }

        this.categoryDAO.delete(UUID.fromString(categoryId));

        return new ApiResponseService(HttpStatus.ACCEPTED, "Category has been removed");
    }

}
