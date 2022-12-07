package tiimae.webshop.iprwc.controller;

import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tiimae.webshop.iprwc.DAO.BrandDAO;
import tiimae.webshop.iprwc.DTO.BrandDTO;
import tiimae.webshop.iprwc.constants.ApiConstant;
import tiimae.webshop.iprwc.service.ApiResponseService;

import javax.servlet.annotation.MultipartConfig;
import java.io.IOException;
import java.util.UUID;

@RestController
public class BrandController {

    private BrandDAO brandDAO;

    public BrandController(BrandDAO brandDAO) {
        this.brandDAO = brandDAO;
    }

    @PostMapping(ApiConstant.getAllBrands)
    @ResponseBody
    public ApiResponseService post(@RequestBody BrandDTO brandDTO) throws IOException {
        return new ApiResponseService(HttpStatus.ACCEPTED, this.brandDAO.postBrand(brandDTO));
    }
}
