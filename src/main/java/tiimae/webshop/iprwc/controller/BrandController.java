package tiimae.webshop.iprwc.controller;

import kong.unirest.json.JSONObject;
import lombok.val;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
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
        final String[] split = brand.getLogoUrl().split("/");
        final String image = this.imageDAO.getImage(brand.getLogoUrl(), brand.getBrandName(), split[split.length - 1]);
        brand.setLogoUrl(image);

        return new ApiResponseService(HttpStatus.ACCEPTED, brand);
    }

    @GetMapping(ApiConstant.getAllBrands)
    @ResponseBody
    public ApiResponseService getAll() throws IOException {
        return new ApiResponseService(HttpStatus.ACCEPTED, this.brandDAO.getAll());
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
}
