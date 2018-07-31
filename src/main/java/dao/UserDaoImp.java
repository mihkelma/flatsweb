package dao;

import model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class UserDaoImp implements UserDao {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public User getUserByUsername(String username) {
        User user;
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
        if (getUserByUsername(user.getUsername()) == null) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            System.out.println("Password was: " + user.getPassword());
            em.persist(user);
        } else {
            em.merge(user);
        }
    }

    @Override
    public void delete(String username) {

    }

    @Override
    public List<User> findAllUsers() {

        List<User> tmp;
        try {
            tmp = em.createQuery("SELECT u FROM User u", User.class).getResultList();
        }
        catch (Exception e) {
            tmp = null;
        }
        return tmp;
    }

    @Override
    public User getUserByToken(String token) {
        User user;
        try {
            user = em.createQuery("SELECT u FROM User u WHERE u.confirmationToken  = :token", User.class)
                    .setParameter("token", token)
                    .getSingleResult();
        }
        catch (NoResultException e) {
            user = null;
        }

        return user;
    }
}
