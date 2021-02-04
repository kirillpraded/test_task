package by.praded.dependency_injection.exception;

/**
 * @author Kiryl Praded
 * 03.02.2021
 */
public class TooManyConstructorsException extends Exception {
    public TooManyConstructorsException(String format) {
        super(format);
    }

    public TooManyConstructorsException() {
        super();
    }
}
