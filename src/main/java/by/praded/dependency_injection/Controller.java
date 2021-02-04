package by.praded.dependency_injection;

import by.praded.dependency_injection.annotation.Inject;
import by.praded.dependency_injection.container.Injector;
import by.praded.dependency_injection.container.impl.InjectorImpl;
import by.praded.dependency_injection.dao.UserDao;
import by.praded.dependency_injection.dao.impl.UserDaoImpl;
import by.praded.dependency_injection.exception.BindingNotFoundException;
import by.praded.dependency_injection.exception.ConstructorNotFoundException;
import by.praded.dependency_injection.exception.TooManyConstructorsException;
import by.praded.dependency_injection.service.UserService;
import by.praded.dependency_injection.service.impl.UserServiceImpl;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.TimeUnit;

/**
 * @author Kiryl Praded
 * 03.02.2021
 */
public class Controller {
    private UserService userService;
    @Inject
    public Controller(UserService userService) {
        this.userService = userService;
    }

    public static void main(String[] args) throws ConstructorNotFoundException, TooManyConstructorsException, IllegalAccessException, InvocationTargetException, InstantiationException, BrokenBarrierException, InterruptedException, BindingNotFoundException {
        UserDao dao = new UserDaoImpl();
        UserDao dao2 = new UserDaoImpl();
        System.out.println(dao.hashCode());
        System.out.println(dao2.hashCode());
    }
}

