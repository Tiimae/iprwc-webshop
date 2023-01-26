package tiimae.webshop.iprwc.DTO;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewDTO {

    @NotNull(message = "Description can't be empty!")
    private String description;

    @NotNull(message = "Stars can't be empty!")
    private int stars;

    @NotNull(message = "Product Id can't be empty!")
    private String productId;

}
