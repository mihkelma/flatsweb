package dao;

import model.Contract;
import model.Unit;
import model.UnitType;
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
                "LEFT JOIN FETCH u.user " +
                "LEFT JOIN FETCH u.unitType WHERE lower(u.user.username) = lower(:username)", Unit.class)
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
                    "LEFT JOIN FETCH u.unitType ut " +
                    "WHERE u.id = :id AND lower(u.user.username) = lower(:username)", Unit.class)
                    .setParameter("username", username)
                    .setParameter("id", id)
                    .getSingleResult();
            return unit;
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    @Transactional
    public void saveUnit(Unit unit, String username) {
        System.out.println("Salvestan unit: " + unit.getId());
        User user = em.find(User.class, username);
        unit.setUser(user);
        if (unit.getId() != null) { //existing unit
            System.out.println("Merging unit!");
            em.merge(unit);
        } else {            //new unit
            System.out.println("New unit");
            unit.setStatus("Active");
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

    @Override
    public List<UnitType> getAllUnitTypes() {
        List<UnitType> unitTypes;
        try {
            unitTypes = em.createQuery("SELECT ut FROM UnitType ut", UnitType.class).getResultList();
        } catch (Exception e) {
            unitTypes = null;
        }
        return unitTypes;
    }
}
