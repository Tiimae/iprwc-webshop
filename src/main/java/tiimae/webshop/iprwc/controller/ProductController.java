package tiimae.webshop.iprwc.controller;

import java.io.IOException;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
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
import lombok.AllArgsConstructor;
import tiimae.webshop.iprwc.DAO.ProductDAO;
import tiimae.webshop.iprwc.DAO.ProductImageDAO;
import tiimae.webshop.iprwc.DTO.ProductDTO;
import tiimae.webshop.iprwc.constants.ApiConstant;
import tiimae.webshop.iprwc.constants.RoleEnum;
import tiimae.webshop.iprwc.exception.EntryAlreadyExistsException;
import tiimae.webshop.iprwc.exception.EntryNotFoundException;
import tiimae.webshop.iprwc.models.Product;
import tiimae.webshop.iprwc.service.response.ApiResponseService;

@RestController
@AllArgsConstructor
public class ProductController {

    private ProductDAO productDAO;
    private ProductImageDAO productImageDAO;

    @GetMapping(ApiConstant.getOneProduct)
    @ResponseBody
    public ApiResponseService get(@PathVariable UUID productId) throws EntryNotFoundException {
        return new ApiResponseService(HttpStatus.ACCEPTED, this.productDAO.get(productId));
    }

    @GetMapping(ApiConstant.getAllProducts)
    @ResponseBody
    public ApiResponseService getAll() {
        return new ApiResponseService(HttpStatus.ACCEPTED, this.productDAO.getAll());
    }

    @PostMapping(ApiConstant.getAllProducts)
    @ResponseBody
    @Secured(RoleEnum.Admin.CODENAME)
    public ApiResponseService post(@RequestParam(value = "product") JSONObject product, @RequestParam(value = "images") MultipartFile[] files) throws IOException, EntryNotFoundException, EntryAlreadyExistsException {
        final ProductDTO productDTO = new ProductDTO();
        productDTO.setName(product.getString("productName"));
        productDTO.setDescription(product.getString("description"));
        productDTO.setPrice(product.getFloat("price"));
        productDTO.setBrandId(UUID.fromString(product.getString("brandId")));
        productDTO.setCategoryId(UUID.fromString(product.getString("categoryId")));
        productDTO.setSupplierId(UUID.fromString(product.getString("supplierId")));

        final Product newProduct = this.productDAO.post(productDTO);

        for (MultipartFile file : files) {
            this.productImageDAO.create(file, newProduct);
        }

        return new ApiResponseService(HttpStatus.ACCEPTED, newProduct);
    }

    @PutMapping(ApiConstant.getOneProduct)
    @ResponseBody
    @Secured(RoleEnum.Admin.CODENAME)
    public ApiResponseService put(
            @PathVariable UUID productId,
            @RequestParam(value = "product") JSONObject product,
            @RequestParam(value = "newImages", required=false) MultipartFile[] files,
            @RequestParam(value = "deletedImages", required=false) String[] deletedFiles
    ) throws IOException, EntryNotFoundException, EntryAlreadyExistsException {
        final ProductDTO productDTO = new ProductDTO();
        productDTO.setName(product.getString("productName"));
        productDTO.setDescription(product.getString("description"));
        productDTO.setPrice(product.getFloat("price"));
        productDTO.setBrandId(UUID.fromString(product.getString("brandId")));
        productDTO.setCategoryId(UUID.fromString(product.getString("categoryId")));
        productDTO.setSupplierId(UUID.fromString(product.getString("supplierId")));

        final Product update = this.productDAO.update(productId, productDTO);
        this.productImageDAO.update(deletedFiles, files, update);

        return new ApiResponseService(HttpStatus.ACCEPTED, update);
    }

    @DeleteMapping(ApiConstant.getOneProduct)
    @ResponseBody
    @Secured(RoleEnum.Admin.CODENAME)
    public ApiResponseService delete(@PathVariable UUID productId) throws IOException, EntryNotFoundException {
        return new ApiResponseService(HttpStatus.ACCEPTED, this.productDAO.delete(productId));
    }

    @DeleteMapping(ApiConstant.restoreOneProduct)
    @ResponseBody
    @Secured(RoleEnum.Admin.CODENAME)
    public ApiResponseService restore(@PathVariable UUID productId) throws IOException, EntryNotFoundException {
        return new ApiResponseService(HttpStatus.ACCEPTED, this.productDAO.restore(productId));
    }
}
