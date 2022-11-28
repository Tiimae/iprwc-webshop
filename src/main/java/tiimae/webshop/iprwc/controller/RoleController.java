package tiimae.webshop.iprwc.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import tiimae.webshop.iprwc.DAO.RoleDAO;
import tiimae.webshop.iprwc.constants.ApiConstant;
import tiimae.webshop.iprwc.models.Role;
import tiimae.webshop.iprwc.service.ApiResponseService;

import java.util.List;

@RestController
@RequestMapping()
public class RoleController {

    private RoleDAO roleDAO;

    public RoleController(RoleDAO roleDAO) {
        this.roleDAO = roleDAO;
    }

    @GetMapping(ApiConstant.getAllRoles)
    @ResponseBody
    public ApiResponseService getAllRoles() {
        final List<Role> allRoles = this.roleDAO.getAllRoles();

        for (Role role : allRoles) {
            role.getUsers().clear();
        }

        return new ApiResponseService(HttpStatus.ACCEPTED, allRoles);
    }

}
