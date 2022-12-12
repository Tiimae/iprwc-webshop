package tiimae.webshop.iprwc.DAO;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import tiimae.webshop.iprwc.DAO.repo.BrandRepository;
import tiimae.webshop.iprwc.DTO.BrandDTO;
import tiimae.webshop.iprwc.mapper.BrandMapper;
import tiimae.webshop.iprwc.models.Brand;

import java.io.IOException;
import java.util.List;
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

    public Brand getBrand(UUID brandId) {
        return this.brandRepository.findById(brandId).get();
    }

    public List<Brand> getAll() {
        return this.brandRepository.findAll();
    }

    public Brand postBrand(BrandDTO brandDTO, MultipartFile file) throws IOException {
        final Brand brand = this.brandMapper.toBrand(brandDTO);

        brand.setLogoUrl(this.imageDAO.saveBrandImage(file, brand.getBrandName(), "brand"));

        return this.brandRepository.save(brand);
    }

    public void delete(UUID brandId) throws IOException {
        final Optional<Brand> byId = this.brandRepository.findById(brandId);

        if (byId.isEmpty()) {
            return;
        }

        final String[] split = byId.get().getLogoUrl().split("/");
        final String file = split[split.length - 1];

        this.imageDAO.deleteImage(byId.get().getBrandName(), file, "brand");

        this.brandRepository.delete(byId.get());
    }

}
