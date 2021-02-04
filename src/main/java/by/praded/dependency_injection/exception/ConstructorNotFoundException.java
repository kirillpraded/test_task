package by.praded.dependency_injection.exception;

/**
 * @author Kiryl Praded
 * 03.02.2021
 */
public class ConstructorNotFoundException extends Exception {
    public ConstructorNotFoundException() {
        super();
    }

    public ConstructorNotFoundException(String message) {
        super(message);
    }
}
