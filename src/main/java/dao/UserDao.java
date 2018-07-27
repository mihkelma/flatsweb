package dao;

import model.User;

import java.util.List;

public interface UserDao {
    User getUserByUsername(String username);
    void save(User user);
    void delete(String username);
    List<User> findAllUsers();
}
