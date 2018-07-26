package dao;

import model.User;

public interface UserDao {
    User findUserByUsername(String username);
    void save(User user);
    void delete(String username);
}
