package by.praded.dependency_injection.service;

import by.praded.dependency_injection.dao.UserDao;

/**
 * @author Kiryl Praded
 * 03.02.2021
 */
public interface UserService {

    void registerUser(String username, char[] password);
    UserDao getUserDao();
}
