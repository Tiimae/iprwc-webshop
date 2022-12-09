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
    private ImageDAO imageDAO;

    public BrandDAO(BrandRepository brandRepository, @Lazy BrandMapper brandMapper, ImageDAO imageDAO) {
        this.brandRepository = brandRepository;
        this.brandMapper = brandMapper;
        this.imageDAO = imageDAO;
    }

    public Brand postBrand(BrandDTO brandDTO, MultipartFile file) throws IOException {
        final Brand brand = this.brandMapper.toBrand(brandDTO);

        brand.setLogoUrl(this.imageDAO.saveBrandImage(file, brand.getBrandName()));

        return this.brandRepository.save(brand);
    }

}
