package tiimae.webshop.iprwc.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import tiimae.webshop.iprwc.DAO.SupplierDAO;
import tiimae.webshop.iprwc.DTO.SupplierDTO;
import tiimae.webshop.iprwc.constants.ApiConstant;
import tiimae.webshop.iprwc.constants.RoleEnum;
import tiimae.webshop.iprwc.service.response.ApiResponseService;

@RestController
@AllArgsConstructor
public class SupplierController {

    private SupplierDAO supplierDAO;

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
    @Secured(RoleEnum.Admin.CODENAME)
    public ApiResponseService post(@RequestBody SupplierDTO supplierDTO) {
        return new ApiResponseService(HttpStatus.CREATED, this.supplierDAO.create(supplierDTO));
    }

    @PutMapping(ApiConstant.getOneSupplier)
    @ResponseBody
    @Secured(RoleEnum.Admin.CODENAME)
    public ApiResponseService put(@PathVariable UUID supplierId, @RequestBody SupplierDTO supplierDTO) {
        return new ApiResponseService(HttpStatus.CREATED, this.supplierDAO.put(supplierId, supplierDTO));
    }

    @DeleteMapping(ApiConstant.getOneSupplier)
    @ResponseBody
    @Secured(RoleEnum.Admin.CODENAME)
    public ApiResponseService delete(@PathVariable UUID supplierId) {
        this.supplierDAO.delete(supplierId);
        return new ApiResponseService(HttpStatus.ACCEPTED, "Supplier has been deleted!");
    }

}
