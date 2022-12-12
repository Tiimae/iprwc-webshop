package tiimae.webshop.iprwc.controller;

import kong.unirest.json.JSONObject;
import lombok.val;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tiimae.webshop.iprwc.DAO.BrandDAO;
import tiimae.webshop.iprwc.DAO.ImageDAO;
import tiimae.webshop.iprwc.DTO.BrandDTO;
import tiimae.webshop.iprwc.constants.ApiConstant;
import tiimae.webshop.iprwc.models.Brand;
import tiimae.webshop.iprwc.service.ApiResponseService;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@RestController
public class BrandController {

    private BrandDAO brandDAO;
    private ImageDAO imageDAO;

    public BrandController(BrandDAO brandDAO, ImageDAO imageDAO) {
        this.brandDAO = brandDAO;
        this.imageDAO = imageDAO;
    }

    @GetMapping(ApiConstant.getOneBrand)
    @ResponseBody
    public ApiResponseService get(@PathVariable UUID brandId) throws IOException {
        final Brand brand = this.brandDAO.getBrand(brandId);
        return new ApiResponseService(HttpStatus.ACCEPTED, brand);
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
        brandDTO.setProductIds(new UUID[0]);
        brandDTO.setLogo("");
        brandDTO.setWebPage(brand.getString("webPage"));

        return new ApiResponseService(HttpStatus.ACCEPTED, this.brandDAO.postBrand(brandDTO, file));
    }

    @PutMapping(value = ApiConstant.getOneBrand)
    @ResponseBody
    public ApiResponseService put(@PathVariable UUID brandId, @RequestParam(value = "brand") JSONObject brand, @RequestParam(value = "logo") @Nullable MultipartFile file) throws IOException {
        final BrandDTO brandDTO = new BrandDTO();
        brandDTO.setBrandName(brand.getString("brandName"));
        brandDTO.setProductIds(new UUID[0]);
        brandDTO.setLogo("");
        brandDTO.setWebPage(brand.getString("webPage"));

        return new ApiResponseService(HttpStatus.ACCEPTED, this.brandDAO.updateBrand(brandId, brandDTO, file));
    }


    @DeleteMapping(ApiConstant.getOneBrand)
    @ResponseBody
    public ApiResponseService delete(@PathVariable UUID brandId) throws IOException {
        this.brandDAO.delete(brandId);
        return new ApiResponseService(HttpStatus.ACCEPTED, "Brand has been deleted");
    }
}
