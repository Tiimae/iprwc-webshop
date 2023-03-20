package tiimae.webshop.iprwc.controller;

import kong.unirest.json.JSONObject;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tiimae.webshop.iprwc.DAO.ProductDAO;
import tiimae.webshop.iprwc.DAO.ProductImageDAO;
import tiimae.webshop.iprwc.DTO.ProductDTO;
import tiimae.webshop.iprwc.constants.ApiConstant;
import tiimae.webshop.iprwc.constants.RoleEnum;
import tiimae.webshop.iprwc.exception.EntryAlreadyExistsException;
import tiimae.webshop.iprwc.exception.EntryNotFoundException;
import tiimae.webshop.iprwc.exception.InvalidDtoException;
import tiimae.webshop.iprwc.exception.uuid.NotAValidUUIDException;
import tiimae.webshop.iprwc.models.Product;
import tiimae.webshop.iprwc.service.response.ApiResponseService;
import tiimae.webshop.iprwc.validators.ProductValidator;

import java.io.IOException;
import java.util.UUID;

@RestController
@AllArgsConstructor
public class ProductController {

    private ProductDAO productDAO;
    private ProductImageDAO productImageDAO;
    private ProductValidator productValidator;

    @GetMapping(ApiConstant.getOneProduct)
    @ResponseBody
    public ApiResponseService get(@PathVariable String productId) throws EntryNotFoundException, NotAValidUUIDException {
        final UUID uuid = this.productValidator.checkIfStringIsUUID(productId);
        return new ApiResponseService(HttpStatus.ACCEPTED, this.productDAO.get(uuid));
    }

    @GetMapping(ApiConstant.getAllProducts)
    @ResponseBody
    public ApiResponseService getAll() {
        return new ApiResponseService(HttpStatus.ACCEPTED, this.productDAO.getAll());
    }

    @PostMapping(ApiConstant.getAllProducts)
    @ResponseBody
    @Secured(RoleEnum.Admin.CODENAME)
    public ApiResponseService post(@RequestParam(value = "product") JSONObject product, @RequestParam(value = "images") MultipartFile[] files) throws IOException, EntryNotFoundException, EntryAlreadyExistsException, InvalidDtoException {
        final ProductDTO productDTO = new ProductDTO();
        productDTO.setName(product.getString("productName"));
        productDTO.setDescription(product.getString("description"));
        productDTO.setPrice(product.getFloat("price"));
        productDTO.setBrandId(UUID.fromString(product.getString("brandId")));
        productDTO.setCategoryId(UUID.fromString(product.getString("categoryId")));
        productDTO.setSupplierId(UUID.fromString(product.getString("supplierId")));

        this.productValidator.validateDTO(productDTO);

        final Product newProduct = this.productDAO.post(productDTO);

        for (MultipartFile file : files) {
            this.productImageDAO.create(file, newProduct);
        }

        final Product productWithImage = this.productDAO.get(newProduct.getId());

        return new ApiResponseService(HttpStatus.ACCEPTED, productWithImage);
    }

    @PutMapping(ApiConstant.getOneProduct)
    @ResponseBody
    @Secured(RoleEnum.Admin.CODENAME)
    public ApiResponseService put(
            @PathVariable String productId,
            @RequestParam(value = "product") JSONObject product,
            @RequestParam(value = "newImages", required=false) MultipartFile[] files,
            @RequestParam(value = "deletedImages", required=false) String[] deletedFiles
    ) throws IOException, EntryNotFoundException, EntryAlreadyExistsException, InvalidDtoException, NotAValidUUIDException {
        final ProductDTO productDTO = new ProductDTO();
        productDTO.setName(product.getString("productName"));
        productDTO.setDescription(product.getString("description"));
        productDTO.setPrice(product.getFloat("price"));
        productDTO.setBrandId(UUID.fromString(product.getString("brandId")));
        productDTO.setCategoryId(UUID.fromString(product.getString("categoryId")));
        productDTO.setSupplierId(UUID.fromString(product.getString("supplierId")));

        final UUID uuid = this.productValidator.checkIfStringIsUUID(productId);
        this.productValidator.validateDTO(productDTO);

        final Product update = this.productDAO.update(uuid, productDTO);
        this.productImageDAO.update(deletedFiles, files, update);

        return new ApiResponseService(HttpStatus.ACCEPTED, update);
    }

    @DeleteMapping(ApiConstant.getOneProduct)
    @ResponseBody
    @Secured(RoleEnum.Admin.CODENAME)
    public ApiResponseService delete(@PathVariable String productId) throws IOException, EntryNotFoundException, NotAValidUUIDException {
        final UUID uuid = this.productValidator.checkIfStringIsUUID(productId);
        return new ApiResponseService(HttpStatus.ACCEPTED, this.productDAO.delete(uuid));
    }

    @DeleteMapping(ApiConstant.restoreOneProduct)
    @ResponseBody
    @Secured(RoleEnum.Admin.CODENAME)
    public ApiResponseService restore(@PathVariable String productId) throws EntryNotFoundException, NotAValidUUIDException {
        final UUID uuid = this.productValidator.checkIfStringIsUUID(productId);
        return new ApiResponseService(HttpStatus.ACCEPTED, this.productDAO.restore(uuid));
    }
}
