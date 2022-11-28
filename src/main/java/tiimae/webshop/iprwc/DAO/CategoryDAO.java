package tiimae.webshop.iprwc.DAO;

import org.springframework.stereotype.Component;
import tiimae.webshop.iprwc.DAO.repo.CategoryRepository;
import tiimae.webshop.iprwc.DTO.CategoryDTO;
import tiimae.webshop.iprwc.mapper.CategoryMapper;
import tiimae.webshop.iprwc.models.Category;
import tiimae.webshop.iprwc.models.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class CategoryDAO {

    private CategoryRepository categoryRepository;
    private CategoryMapper categoryMapper;

    public CategoryDAO(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    public Category get(UUID id) {
        return this.categoryRepository.findById(id).get();
    }

    public List<Category> allCategories() {
        return this.categoryRepository.findAll();
    }

    public Category create(Category category) {
        return this.categoryRepository.save(category);
    }

    public Category update(UUID id, CategoryDTO categoryDTO) {
        final Optional<Category> byId = this.categoryRepository.findById(id);

        if (byId.isEmpty()) {
            return null;
        }

        final Category category = this.categoryMapper.toCategory(categoryDTO);
        category.setId(id);

        return this.categoryRepository.saveAndFlush(category);
    }

    public void delete(UUID id) {
        final Optional<Category> byId = this.categoryRepository.findById(id);

        if (byId.isEmpty()) {
            return;
        }

        byId.get().getProducts().clear();

        this.categoryRepository.delete(byId.get());
    }
}
