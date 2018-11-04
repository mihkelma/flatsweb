package dao;

import model.Role;

public interface RoleDao {
    Role getRoleByRoleName(String roleName);
}
