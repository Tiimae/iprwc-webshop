package tiimae.webshop.iprwc.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tiimae.webshop.iprwc.DAO.SupplierDAO;
import tiimae.webshop.iprwc.DTO.SupplierDTO;
import tiimae.webshop.iprwc.constants.ApiConstant;
import tiimae.webshop.iprwc.service.ApiResponseService;

import java.util.UUID;

@RestController
public class SupplierController {

    private SupplierDAO supplierDAO;

    public SupplierController(SupplierDAO supplierDAO) {
        this.supplierDAO = supplierDAO;
    }

    @GetMapping(ApiConstant.getOneSupplier)
    @ResponseBody
    public ApiResponseService get(@PathVariable UUID supplierId) {
        return new ApiResponseService(HttpStatus.FOUND, this.supplierDAO.get(supplierId));
    }

    @GetMapping(ApiConstant.getAllSupplier)
    @ResponseBody
    public ApiResponseService getAll() {
        return new ApiResponseService(HttpStatus.ACCEPTED, this.supplierDAO.getAll());
    }

    @PostMapping(ApiConstant.getAllSupplier)
    @ResponseBody
    public ApiResponseService post(@RequestBody SupplierDTO supplierDTO) {
        return new ApiResponseService(HttpStatus.CREATED, this.supplierDAO.create(supplierDTO));
    }

    @PutMapping(ApiConstant.getOneSupplier)
    @ResponseBody
    public ApiResponseService put(@PathVariable UUID supplierId, @RequestBody SupplierDTO supplierDTO) {
        return new ApiResponseService(HttpStatus.CREATED, this.supplierDAO.put(supplierId, supplierDTO));
    }

    @DeleteMapping(ApiConstant.getOneSupplier)
    @ResponseBody
    public ApiResponseService delete(@PathVariable UUID supplierId) {
        this.supplierDAO.delete(supplierId);
        return new ApiResponseService(HttpStatus.ACCEPTED, "Supplier has been deleted!");
    }

}
