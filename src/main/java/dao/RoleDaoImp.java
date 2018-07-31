package dao;

import model.Role;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class RoleDaoImp implements RoleDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Role getRoleByRoleName(String roleName) {
        Role role;
        try {
            role = em.createQuery("SELECT r FROM Role r WHERE lower(r.role)  = lower(:roleName)", Role.class)
                    .setParameter("roleName", roleName)
                    .getSingleResult();
            System.out.println("Role = " + role.getRole());
        }
        catch (NoResultException e) {
            role = null;
        }
        return role;
    }
}
