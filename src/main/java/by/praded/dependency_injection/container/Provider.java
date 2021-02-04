package by.praded.dependency_injection.container;

/**
 * @author Kiryl Praded
 * 03.02.2021
 * <p>
 * Provider class that keeps an instance of class generated by {@link Injector}.
 */
public interface Provider<T> {

    /**
     * Method to get instance of the T.
     *
     * @return value of T.
     */
    T getInstance();
}

