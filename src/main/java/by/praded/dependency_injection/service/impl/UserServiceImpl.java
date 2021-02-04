package by.praded.dependency_injection.service.impl;

import by.praded.dependency_injection.annotation.Inject;
import by.praded.dependency_injection.dao.UserDao;
import by.praded.dependency_injection.service.UserService;

/**
 * @author Kiryl Praded
 * 03.02.2021
 */
public class UserServiceImpl implements UserService {

    private UserDao userDao;

    @Inject
    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    public void registerUser(String username, char[] password) {

    }

    @Override
    public UserDao getUserDao() {
        return userDao;
    }

}
