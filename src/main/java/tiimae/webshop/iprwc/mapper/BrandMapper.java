package tiimae.webshop.iprwc.mapper;

import org.springframework.stereotype.Component;
import tiimae.webshop.iprwc.DTO.BrandDTO;
import tiimae.webshop.iprwc.models.Brand;

import java.util.HashSet;

@Component
public class BrandMapper {

    public Brand toBrand(BrandDTO brandDTO) {
        return new Brand(brandDTO.getBrandName(), brandDTO.getWebPage(), "", new HashSet<>());
    }

}
