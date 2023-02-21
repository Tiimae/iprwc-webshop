package tiimae.webshop.iprwc.DAO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import tiimae.webshop.iprwc.DAO.repo.CategoryRepository;
import tiimae.webshop.iprwc.DTO.CategoryDTO;
import tiimae.webshop.iprwc.exception.EntryAlreadyExistsException;
import tiimae.webshop.iprwc.exception.EntryNotFoundException;
import tiimae.webshop.iprwc.mapper.CategoryMapper;
import tiimae.webshop.iprwc.models.Category;
import tiimae.webshop.iprwc.models.Product;

@Component
@AllArgsConstructor
public class CategoryDAO {

    private CategoryRepository categoryRepository;
    private CategoryMapper categoryMapper;

    public Category get(UUID id) throws EntryNotFoundException {
        final Optional<Category> byId = this.categoryRepository.findById(id);
        this.checkIfExists(byId);

        return byId.get();
    }

    public Optional<Category> getByName(String categoryName) {
        return this.categoryRepository.findByCategoryName(categoryName);
    }

    public List<Category> allCategories() {
        return this.categoryRepository.findAll();
    }

    public Category create(Category category) throws EntryAlreadyExistsException {
        this.checkIfBrandNameExists(category.getCategoryName(), null);

        return this.categoryRepository.save(category);
    }

    public Category update(UUID id, CategoryDTO categoryDTO) throws EntryAlreadyExistsException, EntryNotFoundException {
        final Optional<Category> byId = this.categoryRepository.findById(id);
        this.checkIfExists(byId);
        this.checkIfBrandNameExists(categoryDTO.getCategoryName(), null);

        final Category category = this.categoryMapper.mergeCategory(byId.get(), categoryDTO);
        return this.categoryRepository.saveAndFlush(category);
    }

    public void delete(UUID id) throws EntryNotFoundException {
        final Optional<Category> byId = this.categoryRepository.findById(id);
        this.checkIfExists(byId);

        for (Product product : byId.get().getProducts()) {
            product.setCategory(null);
        }


        this.categoryRepository.delete(byId.get());
    }

    public void checkIfExists(Optional<Category> category) throws EntryNotFoundException {
        if (category.isEmpty()) {
            throw new EntryNotFoundException("This category has not been found!");
        }
    }

    public void checkIfBrandNameExists(String categoryName, UUID id) throws EntryAlreadyExistsException {
        Optional<Category> categoryByName = this.categoryRepository.findByCategoryName(categoryName);

        if (categoryByName.isPresent()) {
            if (id != null) {
                if (categoryByName.get().getId() != id) {
                    throw new EntryAlreadyExistsException("Category name already exists");
                }
            } else {

                throw new EntryAlreadyExistsException("Category name already exists");
            }
        }
    }
}
