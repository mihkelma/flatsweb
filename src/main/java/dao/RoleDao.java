package ee.pocos.cland.dao;

import model.Role;

public interface RoleDao {
    Role getRoleByRoleName(String roleName);
}
