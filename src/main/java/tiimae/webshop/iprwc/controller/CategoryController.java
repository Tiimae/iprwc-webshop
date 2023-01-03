package tiimae.webshop.iprwc.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tiimae.webshop.iprwc.DAO.CategoryDAO;
import tiimae.webshop.iprwc.DTO.CategoryDTO;
import tiimae.webshop.iprwc.constants.ApiConstant;
import tiimae.webshop.iprwc.mapper.CategoryMapper;
import tiimae.webshop.iprwc.models.Category;
import tiimae.webshop.iprwc.service.ApiResponseService;

import java.util.UUID;

@RestController
public class CategoryController {

    private CategoryDAO categoryDAO;
    private CategoryMapper categoryMapper;

    public CategoryController(CategoryDAO categoryDAO, CategoryMapper categoryMapper) {
        this.categoryDAO = categoryDAO;
        this.categoryMapper = categoryMapper;
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
        final Category category = this.categoryMapper.toCategory(categoryDTO);

        return new ApiResponseService(HttpStatus.CREATED, this.categoryDAO.create(category));
    }

    @PutMapping(ApiConstant.getOneCategories)
    @ResponseBody
    @CrossOrigin
    public ApiResponseService put(@PathVariable UUID categoryId, @RequestBody CategoryDTO categoryDTO) {
        return new ApiResponseService(HttpStatus.ACCEPTED, this.categoryDAO.update(categoryId, categoryDTO));
    }

    @DeleteMapping(ApiConstant.getOneCategories)
    @ResponseBody
    @CrossOrigin
    public ApiResponseService delete(@PathVariable UUID categoryId) {
        this.categoryDAO.delete(categoryId);

        return new ApiResponseService(HttpStatus.ACCEPTED, "Category has been removed");
    }

}
