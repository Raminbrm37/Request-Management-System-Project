package ir.maktab.finalproject.service.user.impl;

import ir.maktab.finalproject.model.entity.Role;
import ir.maktab.finalproject.repository.RoleRepository;
import ir.maktab.finalproject.service.user.RoleService;
import ir.maktab.finalproject.util.exception.RoleAlreadyExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleRepository roleRepository;

//    public void test(Role role){
//        role.getAuthorities().add(Authority.OP_ACCESS_ADMIN);
//        role.getAuthorities().add(Authority.OP_EDIT_USER);
//        roleRepository.save(role);
//    }


    @Override
    public void save(Role entity) {
        roleRepository.save(entity);
    }

    @Override
    public Role findById(Long id) {
        return roleRepository.getOne(id);
    }

    @Override
    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    @Override
    public void remove(Role entity) {
        roleRepository.delete(entity);
    }

    @Override
    public void removeById(Long id) {
        roleRepository.deleteById(id);
    }

    @Override
    public Role findByName(String name) {
        return roleRepository.findByName(name);
    }

    @Override
    public Boolean checkIfRoleExistByName(String name)  {
        return roleRepository.findByName(name)!=null;
    }

    @Override
    public void saveByException(Role role) throws RoleAlreadyExistException {
        if (checkIfRoleExistByName(role.getName())){
            throw new RoleAlreadyExistException("این نقش با این نام وجود دارد"+role.getName());
        }
        roleRepository.save(role);
    }

    public Role setUserRole() {
        return roleRepository.findByName("USER");
    }
}
