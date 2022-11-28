package tiimae.webshop.iprwc.mapper;

import org.springframework.stereotype.Component;
import tiimae.webshop.iprwc.DAO.CategoryDAO;
import tiimae.webshop.iprwc.DTO.CategoryDTO;
import tiimae.webshop.iprwc.models.Category;

import java.util.HashSet;

@Component
public class CategoryMapper {

    public Category toCategory(CategoryDTO categoryDTO) {

        String name = categoryDTO.getCategoryName();
//        final HashSet<> objects = new HashSet<>();

        return new Category(name, new HashSet<>());

    }

}
