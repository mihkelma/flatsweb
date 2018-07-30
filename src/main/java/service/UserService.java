package service;

import dao.UserDao;
import model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

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
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userDao.save(user);
    }

    public User getUserByToken(String token) {
        User user;
        user = userDao.getUserByToken(token);
        return user;
    }
}
