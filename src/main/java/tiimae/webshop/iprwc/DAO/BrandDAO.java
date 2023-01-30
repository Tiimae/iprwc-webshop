package tiimae.webshop.iprwc.DAO;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import tiimae.webshop.iprwc.DAO.repo.BrandRepository;
import tiimae.webshop.iprwc.DTO.BrandDTO;
import tiimae.webshop.iprwc.mapper.BrandMapper;
import tiimae.webshop.iprwc.models.Brand;
import tiimae.webshop.iprwc.models.Product;

@Component
@AllArgsConstructor
public class BrandDAO {

    private BrandRepository brandRepository;
    private BrandMapper brandMapper;
    private ImageDAO imageDAO;

    public Brand getBrand(UUID brandId) {
        return this.brandRepository.findById(brandId).get();
    }

    public Optional<Brand> getBrandByName(String brandName) {
        return this.brandRepository.findBrandByBrandName(brandName);
    }

    public List<Brand> getAll() {
        return this.brandRepository.findAll();
    }

    public Brand postBrand(BrandDTO brandDTO, MultipartFile file) throws IOException {
        final Brand brand = this.brandMapper.toBrand(brandDTO);

        brand.setLogoUrl(this.imageDAO.saveBrandImage(file, brand.getBrandName(), "brand"));

        return this.brandRepository.save(brand);
    }

    public Brand updateBrand(UUID brandId, BrandDTO brandDTO, MultipartFile file) throws IOException {
        final Optional<Brand> byId = this.brandRepository.findById(brandId);

        if (byId.isEmpty()) {
            return null;
        }

        if (file != null) {
            final String[] split = byId.get().getLogoUrl().split("/");
            final String currentFile = split[split.length - 1];

            this.imageDAO.deleteImage(currentFile, "brand");
            brandDTO.setLogo(this.imageDAO.saveBrandImage(file, brandDTO.getBrandName(), "brand"));
        } else {
            brandDTO.setLogo(byId.get().getLogoUrl());
        }

        Brand newBrand = this.brandMapper.mergeBrand(byId.get(), brandDTO);
        return this.brandRepository.saveAndFlush(newBrand);
    }

    public void delete(UUID brandId) throws IOException {
        final Optional<Brand> byId = this.brandRepository.findById(brandId);

        if (byId.isEmpty()) {
            return;
        }

        final String[] split = byId.get().getLogoUrl().split("/");
        final String file = split[split.length - 1];

        this.imageDAO.deleteImage(file, "brand");
        final Brand brand = byId.get();

        for (Product product : brand.getProducts()) {
            product.setBrand(null);
        }

        this.brandRepository.deleteById(brandId);
    }

}
