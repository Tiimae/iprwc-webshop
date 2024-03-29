package tiimae.webshop.iprwc.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import tiimae.webshop.iprwc.DAO.RoleDAO;
import tiimae.webshop.iprwc.constants.ApiConstant;
import tiimae.webshop.iprwc.constants.RoleEnum;
import tiimae.webshop.iprwc.models.Role;
import tiimae.webshop.iprwc.service.response.ApiResponseService;

import java.util.List;

@RestController
@AllArgsConstructor
public class RoleController {

    private RoleDAO roleDAO;

    @GetMapping(ApiConstant.getAllRoles)
    @ResponseBody
    @Secured(RoleEnum.Admin.CODENAME)
    public ApiResponseService getAllRoles() {
        final List<Role> allRoles = this.roleDAO.getAllRoles();

        for (Role role : allRoles) {
            role.getUsers().clear();
        }

        return new ApiResponseService(HttpStatus.ACCEPTED, allRoles);
    }

}
