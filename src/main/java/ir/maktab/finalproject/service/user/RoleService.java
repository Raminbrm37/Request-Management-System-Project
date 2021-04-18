package ir.maktab.finalproject.service.user;

import ir.maktab.finalproject.model.entity.Role;
import ir.maktab.finalproject.service.BaseEntityService;
import ir.maktab.finalproject.util.exception.RoleAlreadyExistException;

public interface RoleService extends BaseEntityService<Role,Long> {
    Role findByName(String name);
    Boolean checkIfRoleExistByName(String name);
    void saveByException(Role role)throws RoleAlreadyExistException;
}
