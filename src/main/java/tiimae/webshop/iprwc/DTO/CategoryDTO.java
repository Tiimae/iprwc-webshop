package tiimae.webshop.iprwc.DTO;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryDTO {

    @NotNull(message = "Category name can't be empty")
    String categoryName;

    String[] productIds;
}
