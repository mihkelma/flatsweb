package dao;

import model.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Repository
public class UserDaoImp implements UserDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public User findUserByUsername(String username) {
        //TODO: find out, how this is done in other repositories
        User user;
        System.out.println("Userdao - finduserbyusername");
        try {
            user = em.createQuery("SELECT u FROM User u WHERE lower(u.username)  = lower(:username)", User.class)
                    .setParameter("username", username)
                    .getSingleResult();
            System.out.println("User =" + user.getUsername());
        }
        catch (NoResultException e) {
            user = null;
        }

        return user;
    }

    @Override
    @Transactional
    public void save(User user) {

        if (findUserByUsername(user.getUsername()) == null) {
            em.persist(user);
        } else {
            em.merge(user);
        }

        em.persist(user);
    }

    @Override
    public void delete(String username) {

    }
}
