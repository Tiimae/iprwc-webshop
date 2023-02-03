package tiimae.webshop.iprwc.mapper;

import java.util.HashSet;

import org.springframework.stereotype.Component;

import tiimae.webshop.iprwc.DTO.SupplierDTO;
import tiimae.webshop.iprwc.models.Supplier;

@Component
public class SupplierMapper {

    public Supplier toSupplier(SupplierDTO supplierDTO) {
        return new Supplier(supplierDTO.getName(), supplierDTO.getAddress(), supplierDTO.getZipcode(), supplierDTO.getCity(), supplierDTO.getCountry(), new HashSet<>());
    }

    public Supplier mergeSupplier(Supplier base, SupplierDTO update) {
        base.setName(update.getName());
        base.setAddress(update.getAddress());
        base.setZipcode(update.getZipcode());
        base.setCity(update.getCity());
        base.setCountry(update.getCountry());

        return base;
    }

}
