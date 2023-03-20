package tiimae.webshop.iprwc.mapper;

import org.springframework.stereotype.Component;
import tiimae.webshop.iprwc.DTO.CategoryDTO;
import tiimae.webshop.iprwc.models.Category;

import java.util.HashSet;

@Component
public class CategoryMapper {

    public Category toCategory(CategoryDTO categoryDTO) {

        String name = categoryDTO.getCategoryName();

        return new Category(name, new HashSet<>());

    }

    public Category mergeCategory(Category base, CategoryDTO update) {
        base.setCategoryName(update.getCategoryName());

        return base;
    }

}
