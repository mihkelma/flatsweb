package service;

import dao.UserDao;
import model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;


    public List<User> getAllUsers() {
        List<User> tmp = userDao.findAllUsers();
        return tmp;
    }

    public User getUserByUsername(String username) {
        User user;
        user = userDao.getUserByUsername(username);
        return user;
    }

    public void save(User user) {
        userDao.save(user);
    }

    public User getUserByToken(String token) {
        User user;
        user = userDao.getUserByToken(token);
        return user;
    }
}
