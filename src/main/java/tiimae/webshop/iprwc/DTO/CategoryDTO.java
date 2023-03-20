package tiimae.webshop.iprwc.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryDTO {
    private String categoryName;

    private String[] productIds;
}
