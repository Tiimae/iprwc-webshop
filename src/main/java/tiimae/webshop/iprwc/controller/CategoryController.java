package tiimae.webshop.iprwc.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tiimae.webshop.iprwc.DAO.CategoryDAO;
import tiimae.webshop.iprwc.DTO.CategoryDTO;
import tiimae.webshop.iprwc.constants.ApiConstant;
import tiimae.webshop.iprwc.mapper.CategoryMapper;
import tiimae.webshop.iprwc.models.Category;
import tiimae.webshop.iprwc.service.ApiResponseService;
import tiimae.webshop.iprwc.validators.CategoryValidator;

import java.util.UUID;

@RestController
public class CategoryController {

    private CategoryDAO categoryDAO;
    private CategoryMapper categoryMapper;
    private CategoryValidator categoryValidator;

    public CategoryController(CategoryDAO categoryDAO, CategoryMapper categoryMapper, CategoryValidator categoryValidator) {
        this.categoryDAO = categoryDAO;
        this.categoryMapper = categoryMapper;
        this.categoryValidator = categoryValidator;
    }

    @GetMapping(ApiConstant.getOneCategories)
    @ResponseBody
    public ApiResponseService get(@PathVariable UUID categoryId) {
        return new ApiResponseService(HttpStatus.ACCEPTED, this.categoryDAO.get(categoryId));
    }

    @GetMapping(ApiConstant.getAllCategories)
    @ResponseBody
    public ApiResponseService getAll() {
        return new ApiResponseService(HttpStatus.ACCEPTED, this.categoryDAO.allCategories());
    }

    @PostMapping(ApiConstant.getAllCategories)
    @ResponseBody
    public ApiResponseService post(@RequestBody CategoryDTO categoryDTO) {
        final String validate = this.categoryValidator.validateDTO(categoryDTO);

        if (validate != null) {
            return new ApiResponseService(HttpStatus.BAD_REQUEST, validate);
        }

        final Category category = this.categoryMapper.toCategory(categoryDTO);

        return new ApiResponseService(HttpStatus.CREATED, this.categoryDAO.create(category));
    }

    @PutMapping(ApiConstant.getOneCategories)
    @ResponseBody
    @CrossOrigin
    public ApiResponseService put(@PathVariable UUID categoryId, @RequestBody CategoryDTO categoryDTO) {
        final String validate = this.categoryValidator.validateDTO(categoryDTO);

        if (validate != null) {
            return new ApiResponseService(HttpStatus.BAD_REQUEST, validate);
        }

        final String validateId = this.categoryValidator.validateId(categoryId);

        if (validateId != null) {
            return new ApiResponseService(HttpStatus.BAD_REQUEST, validateId);
        }

        return new ApiResponseService(HttpStatus.ACCEPTED, this.categoryDAO.update(categoryId, categoryDTO));
    }

    @DeleteMapping(ApiConstant.getOneCategories)
    @ResponseBody
    @CrossOrigin
    public ApiResponseService delete(@PathVariable UUID categoryId) {
        final String validateId = this.categoryValidator.validateId(categoryId);

        if (validateId != null) {
            return new ApiResponseService(HttpStatus.BAD_REQUEST, validateId);
        }

        this.categoryDAO.delete(categoryId);

        return new ApiResponseService(HttpStatus.ACCEPTED, "Category has been removed");
    }

}
