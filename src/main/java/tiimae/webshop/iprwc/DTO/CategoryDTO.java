package tiimae.webshop.iprwc.DTO;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CategoryDTO {

    @NotNull(message = "Category name can't be empty")
    String categoryName;

    String[] productIds;
}
