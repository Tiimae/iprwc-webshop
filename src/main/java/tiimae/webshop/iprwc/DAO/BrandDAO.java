package tiimae.webshop.iprwc.DAO;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import tiimae.webshop.iprwc.DAO.repo.BrandRepository;
import tiimae.webshop.iprwc.DTO.BrandDTO;
import tiimae.webshop.iprwc.exception.EntryAlreadyExistsException;
import tiimae.webshop.iprwc.exception.EntryNotFoundException;
import tiimae.webshop.iprwc.mapper.BrandMapper;
import tiimae.webshop.iprwc.models.Brand;
import tiimae.webshop.iprwc.models.Product;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@AllArgsConstructor
public class BrandDAO {

    private BrandRepository brandRepository;
    private BrandMapper brandMapper;
    private ImageDAO imageDAO;

    public Brand getBrand(UUID brandId) throws EntryNotFoundException {
        final Optional<Brand> byId = this.brandRepository.findById(brandId);
        this.checkIfExists(byId);
        return byId.get();
    }

    public List<Brand> getAll() {
        return this.brandRepository.findAll();
    }

    public Brand postBrand(BrandDTO brandDTO, MultipartFile file) throws IOException, EntryAlreadyExistsException {
        this.checkIfBrandNameExists(brandDTO.getBrandName(), null);
        final Brand brand = this.brandMapper.toBrand(brandDTO);

        brand.setLogoUrl(this.imageDAO.saveBrandImage(file, brand.getBrandName(), "brand"));

        return this.brandRepository.save(brand);
    }

    @Transactional
    public Brand updateBrand(UUID brandId, BrandDTO brandDTO, MultipartFile file) throws IOException, EntryAlreadyExistsException, EntryNotFoundException {
        final Optional<Brand> byId = this.brandRepository.findById(brandId);
        this.checkIfExists(byId);
        this.checkIfBrandNameExists(brandDTO.getBrandName(), brandId);

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

    @Transactional
    public void delete(UUID brandId) throws IOException, EntryNotFoundException {
        final Optional<Brand> byId = this.brandRepository.findById(brandId);
        this.checkIfExists(byId);

        final String[] split = byId.get().getLogoUrl().split("/");
        final String file = split[split.length - 1];

        this.imageDAO.deleteImage(file, "brand");
        final Brand brand = byId.get();

        for (Product product : brand.getProducts()) {
            product.setBrand(null);
        }

        this.brandRepository.deleteById(brandId);
    }

    public void checkIfExists(Optional<Brand> brand) throws EntryNotFoundException {
        if (brand.isEmpty()) {
            throw new EntryNotFoundException("This brand has not been found!");
        }
    }

    public void checkIfBrandNameExists(String brandName, UUID id) throws EntryAlreadyExistsException {
        Optional<Brand> brandByName = this.brandRepository.findBrandByBrandName(brandName);

        if (brandByName.isPresent()) {
            if (id != null) {
                if (!brandByName.get().getId().equals(id)) {
                    throw new EntryAlreadyExistsException("Brand name already exists");
                }
            } else {

                throw new EntryAlreadyExistsException("Brand name already exists");
            }
        }
    }

}
