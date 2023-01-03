package tiimae.webshop.iprwc.DTO;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Getter
@Setter
public class BrandDTO {

    private String brandName;
    private String webPage;
    private String logo;

    private UUID[] productIds;

}
