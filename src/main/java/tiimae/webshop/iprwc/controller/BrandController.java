package tiimae.webshop.iprwc.controller;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import kong.unirest.json.JSONObject;
import tiimae.webshop.iprwc.DAO.BrandDAO;
import tiimae.webshop.iprwc.DTO.BrandDTO;
import tiimae.webshop.iprwc.constants.ApiConstant;
import tiimae.webshop.iprwc.models.Brand;
import tiimae.webshop.iprwc.service.ApiResponseService;
import tiimae.webshop.iprwc.validators.BrandValidator;

@RestController
public class BrandController {

    private BrandDAO brandDAO;
    private BrandValidator brandValidator;

    public BrandController(BrandDAO brandDAO, BrandValidator brandValidator) {
        this.brandDAO = brandDAO;
        this.brandValidator = brandValidator;
    }

    @GetMapping(ApiConstant.getOneBrand)
    @ResponseBody
    public ApiResponseService get(@PathVariable String brandId) throws IOException {
        String idValidateString = this.brandValidator.validateId(brandId);

        if (idValidateString == null) {
            return new ApiResponseService(HttpStatus.BAD_REQUEST, idValidateString);
        }

        return new ApiResponseService(HttpStatus.ACCEPTED, this.brandDAO.getBrand(UUID.fromString(brandId)));
    }

    @GetMapping(ApiConstant.getAllBrands)
    @ResponseBody
    public ApiResponseService getAll() throws IOException {
        final List<Brand> all = this.brandDAO.getAll();

        return new ApiResponseService(HttpStatus.ACCEPTED, all);
    }

    @PostMapping(ApiConstant.getAllBrands)
    @ResponseBody
    public ApiResponseService post(@RequestParam(value = "brand") JSONObject brand, @RequestParam(value = "logo") MultipartFile file) throws IOException {
        final BrandDTO brandDTO = new BrandDTO();
        brandDTO.setBrandName(brand.getString("brandName"));
        brandDTO.setProductIds(new String[0]);
        brandDTO.setLogo("");
        brandDTO.setWebPage(brand.getString("webPage"));

        String validationString = this.brandValidator.validateDTO(brandDTO);

        if (validationString == null) {
            return new ApiResponseService(HttpStatus.BAD_REQUEST, validationString);
        }


        return new ApiResponseService(HttpStatus.ACCEPTED, this.brandDAO.postBrand(brandDTO, file));
    }

    @PutMapping(value = ApiConstant.getOneBrand)
    @ResponseBody
    public ApiResponseService put(@PathVariable String brandId, @RequestParam(value = "brand") JSONObject brand, @RequestParam(value = "logo") @Nullable MultipartFile file) throws IOException {
        String idValidateString = this.brandValidator.validateId(brandId);

        if (idValidateString == null) {
            return new ApiResponseService(HttpStatus.BAD_REQUEST, idValidateString);
        }
        
        final BrandDTO brandDTO = new BrandDTO();
        brandDTO.setBrandName(brand.getString("brandName"));
        brandDTO.setProductIds(new String[0]);
        brandDTO.setLogo("");
        brandDTO.setWebPage(brand.getString("webPage"));

        return new ApiResponseService(HttpStatus.ACCEPTED, this.brandDAO.updateBrand(UUID.fromString(brandId), brandDTO, file));
    }


    @DeleteMapping(ApiConstant.getOneBrand)
    @ResponseBody
    public ApiResponseService delete(@PathVariable String brandId) throws IOException {
        String idValidateString = this.brandValidator.validateId(brandId);

        if (idValidateString == null) {
            return new ApiResponseService(HttpStatus.BAD_REQUEST, idValidateString);
        }

        this.brandDAO.delete(UUID.fromString(brandId));
        return new ApiResponseService(HttpStatus.ACCEPTED, "Brand has been deleted");
    }
}
