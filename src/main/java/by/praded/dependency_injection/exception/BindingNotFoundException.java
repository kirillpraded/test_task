package by.praded.dependency_injection.exception;

/**
 * @author Kiryl Praded
 * 03.02.2021
 */
public class BindingNotFoundException extends Exception {
    public BindingNotFoundException() {
        super();
    }

    public BindingNotFoundException(String message) {
        super(message);
    }
}
