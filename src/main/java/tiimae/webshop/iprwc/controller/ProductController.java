package tiimae.webshop.iprwc.controller;

import kong.unirest.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tiimae.webshop.iprwc.DAO.ProductDAO;
import tiimae.webshop.iprwc.DAO.ProductImageDAO;
import tiimae.webshop.iprwc.DTO.ProductDTO;
import tiimae.webshop.iprwc.constants.ApiConstant;
import tiimae.webshop.iprwc.models.Product;
import tiimae.webshop.iprwc.service.ApiResponseService;

import java.io.IOException;
import java.util.UUID;

@RestController
public class ProductController {

    private ProductDAO productDAO;
    private ProductImageDAO productImageDAO;

    public ProductController(ProductDAO productDAO, ProductImageDAO productImageDAO) {
        this.productDAO = productDAO;
        this.productImageDAO = productImageDAO;
    }

    @GetMapping(ApiConstant.getOneProduct)
    @ResponseBody
    public ApiResponseService get(@PathVariable UUID productId) {
        return new ApiResponseService(HttpStatus.FOUND, this.productDAO.get(productId));
    }

    @GetMapping(ApiConstant.getAllProducts)
    @ResponseBody
    public ApiResponseService getAll() {
        return new ApiResponseService(HttpStatus.FOUND, this.productDAO.getAll());
    }

    @PostMapping(ApiConstant.getAllProducts)
    @ResponseBody
    public ApiResponseService post(@RequestParam(value = "product") JSONObject product, @RequestParam(value = "images") MultipartFile[] files) throws IOException {
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

        return new ApiResponseService(HttpStatus.FOUND, newProduct);
    }

    @PutMapping(ApiConstant.getOneProduct)
    @ResponseBody
    public ApiResponseService put(
            @PathVariable UUID productId,
            @RequestParam(value = "product") JSONObject product,
            @RequestParam(value = "newImages", required=false) MultipartFile[] files,
            @RequestParam(value = "deletedImages", required=false) String[] deletedFiles
    ) throws IOException {
        final ProductDTO productDTO = new ProductDTO();
        productDTO.setName(product.getString("productName"));
        productDTO.setDescription(product.getString("description"));
        productDTO.setPrice(product.getFloat("price"));
        productDTO.setBrandId(UUID.fromString(product.getString("brandId")));
        productDTO.setCategoryId(UUID.fromString(product.getString("categoryId")));
        productDTO.setSupplierId(UUID.fromString(product.getString("supplierId")));



        final Product update = this.productDAO.update(productId, productDTO);
        this.productImageDAO.update(deletedFiles, files, update);

        return new ApiResponseService(HttpStatus.FOUND, update);
    }

    @DeleteMapping(ApiConstant.getOneProduct)
    @ResponseBody
    public ApiResponseService delete(@PathVariable UUID productId) throws IOException {
        final Product product = this.productDAO.get(productId);
        this.productImageDAO.delete(productId, product);

        this.productDAO.delete(productId);
        return new ApiResponseService(HttpStatus.FOUND, "Product has been deleted");
    }
}
