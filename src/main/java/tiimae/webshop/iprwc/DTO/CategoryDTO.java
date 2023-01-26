package tiimae.webshop.iprwc.DTO;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.UniqueElements;
import tiimae.webshop.iprwc.validators.CategoryValidator;

import javax.validation.constraints.NotNull;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
public class CategoryDTO {

    @NotNull
    String categoryName;


    String[] productIds;
}
