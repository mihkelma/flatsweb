package dao;

import model.Unit;
import model.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class UnitDaoImp implements UnitDao {
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Unit> getAllUserUnits(String username) {
        List<Unit> units;
        units = em.createQuery("SELECT u FROM Unit u " +
                "LEFT JOIN FETCH u.user WHERE lower(u.user.username) = lower(:username) ", Unit.class)
                .setParameter("username", username)
                .getResultList();
        if (units.isEmpty()) return null;
        return units;
    }

    @Override
    public Unit getUnitById(Long id, String username) {
        try {
            Unit unit;
            unit = em.createQuery("SELECT u FROM Unit u " +
                    "WHERE lower(u.user.username) = lower(:username)", Unit.class)
                    .setParameter("username", username)
                    .getSingleResult();
            return unit;
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    @Transactional
    public void saveUnit(Unit unit, String username) {
        if (unit.getId() != null) { //existing unit
            em.merge(unit);
        } else {                        //new unit
            User user = em.find(User.class, username);
            unit.setStatus("Active");
            unit.setUser(user);
            em.persist(unit);
        }
    }

    @Override
    @Transactional
    public void deleteUnit(Long id, String username) {
        Unit unit = em.find(Unit.class, id);
        //TODO: instead of removing, change status and merge
        em.remove(unit);
    }
}
