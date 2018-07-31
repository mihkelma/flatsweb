package service;

import dao.RoleDao;
import model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    @Autowired
    private RoleDao roleDao;

    public Role getRoleByRoleName(String roleName) {
        Role role;
        role = roleDao.getRoleByRoleName(roleName);
        return role;
    }
}
