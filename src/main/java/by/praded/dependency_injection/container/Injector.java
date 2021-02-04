package by.praded.dependency_injection.container;

import by.praded.dependency_injection.exception.BindingNotFoundException;
import by.praded.dependency_injection.exception.ConstructorNotFoundException;
import by.praded.dependency_injection.exception.TooManyConstructorsException;

/**
 * @author Kiryl Praded
 * 03.02.2021
 * <p>
 * Simple Dependency Injector.
 */
public interface Injector {
    /**
     * Method to create provider of given class.
     *
     * @param type - class to create an instance.
     * @param <T>  - type of the class to create.
     * @return - {@link Provider} that keeps created object.
     * @throws TooManyConstructorsException - may throws when class contains
     *                                      more then one constructor annotated with {@link by.praded.dependency_injection.annotation.Inject}.
     * @throws ConstructorNotFoundException - may throws when class don't contains default constructor and constructor
     *                                      annotated with {@link by.praded.dependency_injection.annotation.Inject}.
     * @throws BindingNotFoundException     - may throws when class to bind is not found.
     */
    <T> Provider<T> getProvider(Class<T> type) throws TooManyConstructorsException,
            ConstructorNotFoundException,
            BindingNotFoundException; //получение инстанса класса со всеми иньекциями по классу интерфейса

    /**
     * Method to add class and its implementation to bindings.
     *
     * @param interfaceClass - class of interface to add.
     * @param impl           - class of implementation of given interface to add.
     * @param <T>            - type of the interface.
     */
    <T> void bind(Class<T> interfaceClass, Class<? extends T> impl); //регистрация байндинга по классу интерфейса и его реализации

    /**
     * Method to add class and its implementation to bindings of singletones.
     * @param interfaceClass - class of interface to add.
     * @param impl           - class of implementation of given interface to add.
     * @param <T>            - type of the interface.
     */
    <T> void bindSingleton(Class<T> interfaceClass, Class<? extends T> impl); //регистрация синглтон класса
}
