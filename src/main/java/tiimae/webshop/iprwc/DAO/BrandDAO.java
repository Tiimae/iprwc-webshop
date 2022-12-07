package tiimae.webshop.iprwc.DAO;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import tiimae.webshop.iprwc.DAO.repo.BrandRepository;
import tiimae.webshop.iprwc.DTO.BrandDTO;
import tiimae.webshop.iprwc.mapper.BrandMapper;
import tiimae.webshop.iprwc.models.Brand;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Component
public class BrandDAO {

    private BrandRepository brandRepository;
    private BrandMapper brandMapper;

    public BrandDAO(BrandRepository brandRepository, @Lazy BrandMapper brandMapper) {
        this.brandRepository = brandRepository;
        this.brandMapper = brandMapper;
    }

    public Brand postBrand(BrandDTO brandDTO) throws IOException {
        final Brand brand = this.brandMapper.toBrand(brandDTO);

        return this.brandRepository.save(brand);
    }

}
