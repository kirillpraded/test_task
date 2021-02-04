package by.praded.dependency_injection.container;

import by.praded.dependency_injection.container.impl.InjectorImpl;
import by.praded.dependency_injection.dao.UserDao;
import by.praded.dependency_injection.dao.impl.UserDaoImpl;
import by.praded.dependency_injection.exception.BindingNotFoundException;
import by.praded.dependency_injection.exception.ConstructorNotFoundException;
import by.praded.dependency_injection.exception.TooManyConstructorsException;
import by.praded.dependency_injection.service.AnswerService;
import by.praded.dependency_injection.service.CategoryService;
import by.praded.dependency_injection.service.UserService;
import by.praded.dependency_injection.service.impl.AnswerServiceImpl;
import by.praded.dependency_injection.service.impl.CategoryServiceImpl;
import by.praded.dependency_injection.service.impl.UserServiceImpl;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author Kiryl Praded
 * 04.02.2021
 */
public class InjectorImplTest {
    @Test(description = "test successful creating an example of class without any dependencies")
    public void testExistingBinding() throws BindingNotFoundException, TooManyConstructorsException, ConstructorNotFoundException {
        Injector injector = new InjectorImpl(); //создаем имплементацию инжектора
        injector.bind(UserDao.class, UserDaoImpl.class); //добавляем в инжектор реализацию интерфейса
        Provider<UserDao> daoProvider = injector.getProvider(UserDao.class); //получаем инстанс класса из инжектора
        Assert.assertNotNull(daoProvider);
        Assert.assertNotNull(daoProvider.getInstance());
        Assert.assertSame(UserDaoImpl.class, daoProvider.getInstance().getClass());
    }


    @Test(description = "test successful creating an example of class with dependency")
    public void testExistingBindingWithDependency() throws BindingNotFoundException, TooManyConstructorsException, ConstructorNotFoundException {
        Injector injector = new InjectorImpl(); //создаем имплементацию инжектора
        injector.bind(UserDao.class, UserDaoImpl.class); //добавляем в инжектор реализацию интерфейса
        injector.bind(UserService.class, UserServiceImpl.class); //Юзер сервис зависит от юзер дао(принимает в качестве аргумента в конструкторе)
        Provider<UserService> serviceProvider = injector.getProvider(UserService.class); //получаем инстанс класса из инжектора
        Assert.assertNotNull(serviceProvider);
        Assert.assertNotNull(serviceProvider.getInstance());
        Assert.assertSame(UserServiceImpl.class, serviceProvider.getInstance().getClass());
        Assert.assertSame(UserDaoImpl.class, serviceProvider.getInstance().getUserDao().getClass());
    }

    @Test(description = "test successful creating an example of class that depends from singleton object")
    public void testExistingBindingWithSingletonDependency() throws BindingNotFoundException, TooManyConstructorsException, ConstructorNotFoundException {
        Injector injector = new InjectorImpl(); //создаем имплементацию инжектора
        injector.bindSingleton(UserDao.class, UserDaoImpl.class); //добавляем в инжектор реализацию интерфейса
        injector.bind(UserService.class, UserServiceImpl.class); //Юзер сервис зависит от юзер дао(принимает в качестве аргумента в конструкторе)
        Provider<UserService> serviceProvider = injector.getProvider(UserService.class); //получаем инстанс класса из инжектора
        Assert.assertNotNull(serviceProvider);
        Assert.assertNotNull(serviceProvider.getInstance());
        Assert.assertSame(UserServiceImpl.class, serviceProvider.getInstance().getClass());
        Assert.assertSame(UserDaoImpl.class, serviceProvider.getInstance().getUserDao().getClass());
    }

    @Test(description = "test creating singleton objects")
    public void testCreatingSingletonObjects() throws BindingNotFoundException, TooManyConstructorsException, ConstructorNotFoundException {
        Injector injector = new InjectorImpl(); //создаем имплементацию инжектора
        injector.bindSingleton(UserDao.class, UserDaoImpl.class); //добавляем в инжектор реализацию интерфейса
        injector.bindSingleton(UserService.class, UserServiceImpl.class); //Пусть мы хотим, чтобы сервис был в единственном экземпляре для всего контейнера
        Provider<UserService> firstServiceProvider = injector.getProvider(UserService.class);
        Provider<UserService> secondServiceProvider = injector.getProvider(UserService.class); //получаем инстанс класса из инжектора
        Assert.assertNotNull(firstServiceProvider);
        Assert.assertNotNull(firstServiceProvider.getInstance());

        Assert.assertNotNull(secondServiceProvider);
        Assert.assertNotNull(secondServiceProvider.getInstance());

        Assert.assertSame(UserServiceImpl.class, firstServiceProvider.getInstance().getClass());
        Assert.assertSame(UserDaoImpl.class, firstServiceProvider.getInstance().getUserDao().getClass());

        Assert.assertSame(UserServiceImpl.class, secondServiceProvider.getInstance().getClass());
        Assert.assertSame(UserDaoImpl.class, secondServiceProvider.getInstance().getUserDao().getClass());

        Assert.assertSame(firstServiceProvider.getInstance(), secondServiceProvider.getInstance());
    }

    @Test(description = "don't add binding and expecting binding not found exception")
    public void testNotExistingBindingException() {
        Injector injector = new InjectorImpl(); //создаем имплементацию инжектора
        //ожидаем исключения binding not found
        Assert.assertThrows(BindingNotFoundException.class, () -> injector.getProvider(UserService.class));
    }

    @Test(description = "test getProvider expecting TooManyConstructorsException")
    public void testGetProviderExpectingTooManyConstructorsException() {
        Injector injector = new InjectorImpl(); //создаем имплементацию инжектора
        /*
        добавим класс, содержащий несколько
        конструкторов помеченных аннотаций @Inject
        */
        injector.bind(AnswerService.class, AnswerServiceImpl.class);
        Assert.assertThrows(TooManyConstructorsException.class, () -> injector.getProvider(AnswerService.class));
    }

    @Test
    public void testGetProviderExpectingConstructorNotFoundException() {
        Injector injector = new InjectorImpl(); //создаем имплементацию инжектора
        /*
        добавим класс, не содержащий конструктора помеченного
         аннотаций @Inject и default конструктора
         */
        injector.bind(CategoryService.class, CategoryServiceImpl.class);
        Assert.assertThrows(ConstructorNotFoundException.class, () -> injector.getProvider(CategoryService.class));
    }
}
