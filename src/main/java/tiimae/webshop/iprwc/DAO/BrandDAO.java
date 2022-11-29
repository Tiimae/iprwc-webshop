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

    public Brand postBrand(BrandDTO brandDTO) throws IOException {
        final Brand brand = this.brandMapper.toBrand(brandDTO);

        return this.brandRepository.save(brand);
    }

    public Brand postBrandImage(UUID id, MultipartFile logo) throws IOException {
        final Optional<Brand> byId = this.brandRepository.findById(id);
        final String path = this.imageDAO.saveBrandImage(logo);

        if (byId.isEmpty()) {
            return null;
        }

        byId.get().setLogoUrl(path);

        return this.brandRepository.saveAndFlush(byId.get());
    }
}
