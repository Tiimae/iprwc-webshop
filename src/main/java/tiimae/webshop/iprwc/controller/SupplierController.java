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
import tiimae.webshop.iprwc.exception.EntryAlreadyExistsException;
import tiimae.webshop.iprwc.exception.EntryNotFoundException;
import tiimae.webshop.iprwc.exception.InvalidDtoException;
import tiimae.webshop.iprwc.exception.uuid.NotAValidUUIDException;
import tiimae.webshop.iprwc.service.response.ApiResponseService;
import tiimae.webshop.iprwc.validators.SupplierValidator;

@RestController
@AllArgsConstructor
public class SupplierController {

    private SupplierDAO supplierDAO;
    private SupplierValidator supplierValidator;

    @GetMapping(ApiConstant.getOneSupplier)
    @ResponseBody
    public ApiResponseService get(@PathVariable String supplierId) throws EntryNotFoundException, NotAValidUUIDException {
        final UUID id = this.supplierValidator.checkIfStringIsUUID(supplierId);
        return new ApiResponseService(HttpStatus.ACCEPTED, this.supplierDAO.get(id));
    }

    @GetMapping(ApiConstant.getAllSupplier)
    @ResponseBody
    public ApiResponseService getAll() {
        return new ApiResponseService(HttpStatus.ACCEPTED, this.supplierDAO.getAll());
    }

    @PostMapping(ApiConstant.getAllSupplier)
    @ResponseBody
    @Secured(RoleEnum.Admin.CODENAME)
    public ApiResponseService post(@RequestBody SupplierDTO supplierDTO) throws EntryAlreadyExistsException, InvalidDtoException {
        this.supplierValidator.validateDTO(supplierDTO);
        return new ApiResponseService(HttpStatus.ACCEPTED, this.supplierDAO.create(supplierDTO));
    }

    @PutMapping(ApiConstant.getOneSupplier)
    @ResponseBody
    @Secured(RoleEnum.Admin.CODENAME)
    public ApiResponseService put(@PathVariable String supplierId, @RequestBody SupplierDTO supplierDTO) throws EntryNotFoundException, EntryAlreadyExistsException, NotAValidUUIDException, InvalidDtoException {
        final UUID id = this.supplierValidator.checkIfStringIsUUID(supplierId);
        this.supplierValidator.validateDTO(supplierDTO);
        return new ApiResponseService(HttpStatus.ACCEPTED, this.supplierDAO.put(id, supplierDTO));
    }

    @DeleteMapping(ApiConstant.getOneSupplier)
    @ResponseBody
    @Secured(RoleEnum.Admin.CODENAME)
    public ApiResponseService delete(@PathVariable String supplierId) throws EntryNotFoundException, NotAValidUUIDException {
        final UUID id = this.supplierValidator.checkIfStringIsUUID(supplierId);
        this.supplierDAO.delete(id);
        return new ApiResponseService(HttpStatus.ACCEPTED, "Supplier has been deleted!");
    }

}
