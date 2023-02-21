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
import tiimae.webshop.iprwc.exception.EntryAlreadyExistsException;
import tiimae.webshop.iprwc.exception.EntryNotFoundException;
import tiimae.webshop.iprwc.exception.InvalidDtoException;
import tiimae.webshop.iprwc.exception.uuid.NotAValidUUIDException;
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
    public ApiResponseService get(@PathVariable String categoryId) throws NotAValidUUIDException, EntryNotFoundException {
        final UUID id = this.categoryValidator.checkIfStringIsUUID(categoryId);

        return new ApiResponseService(HttpStatus.ACCEPTED, this.categoryDAO.get(id));
    }

    @GetMapping(ApiConstant.getAllCategories)
    @ResponseBody
    public ApiResponseService getAll() {
        return new ApiResponseService(HttpStatus.ACCEPTED, this.categoryDAO.allCategories());
    }

    @PostMapping(ApiConstant.getAllCategories)
    @ResponseBody
    @Secured(RoleEnum.Admin.CODENAME)
    public ApiResponseService post(@RequestBody CategoryDTO categoryDTO) throws InvalidDtoException, EntryAlreadyExistsException {
        this.categoryValidator.validateDTO(categoryDTO);
        final Category category = this.categoryMapper.toCategory(categoryDTO);

        return new ApiResponseService(HttpStatus.ACCEPTED, this.categoryDAO.create(category));
    }

    @PutMapping(ApiConstant.getOneCategories)
    @ResponseBody
    @Secured(RoleEnum.Admin.CODENAME)
    public ApiResponseService put(@PathVariable String categoryId, @RequestBody CategoryDTO categoryDTO) throws NotAValidUUIDException, InvalidDtoException, EntryNotFoundException, EntryAlreadyExistsException {
        final UUID id = this.categoryValidator.checkIfStringIsUUID(categoryId);
        this.categoryValidator.validateDTO(categoryDTO);

        return new ApiResponseService(HttpStatus.ACCEPTED, this.categoryDAO.update(id, categoryDTO));
    }

    @DeleteMapping(ApiConstant.getOneCategories)
    @ResponseBody
    @Secured(RoleEnum.Admin.CODENAME)
    public ApiResponseService delete(@PathVariable String categoryId) throws NotAValidUUIDException, EntryNotFoundException {
        final UUID id = this.categoryValidator.checkIfStringIsUUID(categoryId);

        this.categoryDAO.delete(id);

        return new ApiResponseService(HttpStatus.ACCEPTED, "Category has been removed");
    }

}
