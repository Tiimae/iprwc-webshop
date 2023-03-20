package tiimae.webshop.iprwc.controller;

import kong.unirest.json.JSONObject;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tiimae.webshop.iprwc.DAO.BrandDAO;
import tiimae.webshop.iprwc.DTO.BrandDTO;
import tiimae.webshop.iprwc.constants.ApiConstant;
import tiimae.webshop.iprwc.constants.RoleEnum;
import tiimae.webshop.iprwc.exception.EntryAlreadyExistsException;
import tiimae.webshop.iprwc.exception.EntryNotFoundException;
import tiimae.webshop.iprwc.exception.InvalidDtoException;
import tiimae.webshop.iprwc.exception.uuid.NotAValidUUIDException;
import tiimae.webshop.iprwc.service.response.ApiResponseService;
import tiimae.webshop.iprwc.validators.BrandValidator;

import java.io.IOException;
import java.util.UUID;

@RestController
@AllArgsConstructor
public class BrandController {

    private BrandDAO brandDAO;
    private BrandValidator brandValidator;

    @GetMapping(ApiConstant.getOneBrand)
    @ResponseBody
    @Secured(RoleEnum.Admin.CODENAME)
    public ApiResponseService get(@PathVariable String brandId) throws IOException, NotAValidUUIDException, EntryNotFoundException {
        UUID id = this.brandValidator.checkIfStringIsUUID(brandId);

        return new ApiResponseService(HttpStatus.ACCEPTED, this.brandDAO.getBrand(id));
    }

    @GetMapping(ApiConstant.getAllBrands)
    @ResponseBody
    public ApiResponseService getAll() {
        return new ApiResponseService(HttpStatus.ACCEPTED, this.brandDAO.getAll());
    }

    @PostMapping(ApiConstant.getAllBrands)
    @ResponseBody
    @Secured(RoleEnum.Admin.CODENAME)
    public ApiResponseService post(@RequestParam(value = "brand") JSONObject brand, @RequestParam(value = "logo") MultipartFile file) throws IOException, EntryAlreadyExistsException, InvalidDtoException {
        final BrandDTO brandDTO = new BrandDTO();
        brandDTO.setBrandName(brand.getString("brandName"));
        brandDTO.setProductIds(new String[0]);
        brandDTO.setLogo("");
        brandDTO.setWebPage(brand.getString("webPage"));

        this.brandValidator.validateDTO(brandDTO);


        return new ApiResponseService(HttpStatus.ACCEPTED, this.brandDAO.postBrand(brandDTO, file));
    }

    @PutMapping(value = ApiConstant.getOneBrand)
    @ResponseBody
    @Secured(RoleEnum.Admin.CODENAME)
    public ApiResponseService put(
            @PathVariable String brandId,
            @RequestParam(value = "brand") JSONObject brand,
            @RequestParam(value = "logo") @Nullable MultipartFile file
    ) throws IOException, EntryNotFoundException, EntryAlreadyExistsException, NotAValidUUIDException, InvalidDtoException {
        UUID id = this.brandValidator.checkIfStringIsUUID(brandId);

        final BrandDTO brandDTO = new BrandDTO();
        brandDTO.setBrandName(brand.getString("brandName"));
        brandDTO.setProductIds(new String[0]);
        brandDTO.setLogo("");
        brandDTO.setWebPage(brand.getString("webPage"));

        this.brandValidator.validateDTO(brandDTO);

        return new ApiResponseService(HttpStatus.ACCEPTED, this.brandDAO.updateBrand(id, brandDTO, file));
    }


    @DeleteMapping(ApiConstant.getOneBrand)
    @ResponseBody
    @Secured(RoleEnum.Admin.CODENAME)
    public ApiResponseService delete(@PathVariable String brandId) throws IOException, EntryNotFoundException, NotAValidUUIDException {
        UUID id = this.brandValidator.checkIfStringIsUUID(brandId);

        this.brandDAO.delete(UUID.fromString(brandId));
        return new ApiResponseService(HttpStatus.ACCEPTED, "Brand has been deleted");
    }
}
