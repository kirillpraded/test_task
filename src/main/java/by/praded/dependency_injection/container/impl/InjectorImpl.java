package by.praded.dependency_injection.container.impl;

import by.praded.dependency_injection.annotation.Inject;
import by.praded.dependency_injection.container.Injector;
import by.praded.dependency_injection.container.Provider;
import by.praded.dependency_injection.exception.BindingNotFoundException;
import by.praded.dependency_injection.exception.ConstructorNotFoundException;
import by.praded.dependency_injection.exception.TooManyConstructorsException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Kiryl Praded
 * 03.02.2021
 * <p>
 * Simple Dependency Injector implementation.
 */
public class InjectorImpl implements Injector {
    /**
     * Map of classes with prototype scope.
     */
    private Map<Class<?>, Class<?>> availableClasses;
    /**
     * Map of classes with singleton scope.
     */
    private Map<Class<?>, AbstractMap.SimpleEntry<Class<?>, ?>> singletons;
    /**
     * Lock to ensure thread safety.
     */
    private Lock lock = new ReentrantLock();

    /**
     * Class constructor.
     * Initializes {@link InjectorImpl#availableClasses} and {@link InjectorImpl#singletons}
     */
    public InjectorImpl() {
        availableClasses = new HashMap<>();
        singletons = new HashMap<>();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public <T> Provider<T> getProvider(Class<T> type) throws TooManyConstructorsException, ConstructorNotFoundException, BindingNotFoundException {
        AbstractMap.SimpleEntry<Class<?>, ?> tClassSingleton = singletons.get(type);
        if (Objects.nonNull(tClassSingleton)) {
            lock.lock();
            try {
                tClassSingleton.setValue(provideSingleton(tClassSingleton));
            } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
                lock.unlock();
                throw new ConstructorNotFoundException(e.getMessage());
            }
            lock.unlock();
            return new ProviderImpl<>((T) tClassSingleton.getValue());
        } else {
            Class<?> tClass = availableClasses.get(type);
            if (Objects.isNull(tClass)) {
                throw new BindingNotFoundException(String.format("Cannot create example of the  %s. You have to add it.", type));
            }
            Constructor<?>[] constructors = tClass.getConstructors();
            int amount = 0;
            Constructor<?> constructorToUse = null;
            for (Constructor<?> constructor : constructors) {
                if (Objects.isNull(constructorToUse) && constructor.getParameterTypes().length == 0) {
                    constructorToUse = constructor;
                }
                Annotation[] annotations = constructor.getDeclaredAnnotations();
                for (Annotation annotation : annotations) {
                    if (annotation.annotationType() == Inject.class) {
                        amount++;
                        constructorToUse = constructor;
                    }
                }
            }

            //Если конструкторов, помеченных аннотацией больше одного то выбрасываем исключение
            if (amount > 1) {
                throw new TooManyConstructorsException(String.format("There is too many constructors with annotation %s in class %s", Inject.class, tClass));
            }
            //Если конструкторов помеченных аннотацией нет и конструктора по умолчанию также нет выбрасываем исключение
            if (Objects.isNull(constructorToUse)) {
                throw new ConstructorNotFoundException(String.format("There is no constructors with annotation %s in class %s", Inject.class, tClass));
            }

            Class<?>[] args = constructorToUse.getParameterTypes();
            Object[] initArgs = new Object[args.length];

            for (int i = 0; i < args.length; i++) {
                Class<?> arg = args[i];
                initArgs[i] = getProvider(arg).getInstance();
            }

            T t;
            try {
                if (args.length == 0) {
                    t = (T) constructorToUse.newInstance();
                } else {
                    t = (T) constructorToUse.newInstance(initArgs);
                }
            } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
                throw new ConstructorNotFoundException(e.getMessage());
            }

            return new ProviderImpl<>(t);
        }
    }

    /**
     * Method to provide singleton object.
     *
     * @param entry - entry of impl class as key and value.
     * @param <T>   - type of class to create or get from entry.
     * @return created class.
     * @throws TooManyConstructorsException - may throws when class contains
     *                                      more then one constructor annotated with {@link by.praded.dependency_injection.annotation.Inject}.
     * @throws ConstructorNotFoundException - may throws when class don't contains default constructor and constructor
     *                                      annotated with {@link by.praded.dependency_injection.annotation.Inject}.
     * @throws BindingNotFoundException     - may throws when class to bind is not found.
     * @throws IllegalAccessException       - exception may throws when found constructor isn't accessible.
     * @throws InvocationTargetException    - exception of creating instance of class.
     * @throws InstantiationException       - exception of creating instance of class.
     */
    @SuppressWarnings("unchecked")
    private <T> T provideSingleton(AbstractMap.SimpleEntry<Class<?>, ?> entry) throws TooManyConstructorsException, ConstructorNotFoundException, IllegalAccessException, InvocationTargetException, InstantiationException, BindingNotFoundException {
        if (Objects.nonNull(entry.getValue())) {
            return (T) entry.getValue();
        } else {
            Class<?> tClass = entry.getKey();
            Constructor<?>[] constructors = tClass.getConstructors();
            int amount = 0;
            Constructor<?> constructorToUse = null;
            for (Constructor<?> constructor : constructors) {
                if (Objects.isNull(constructorToUse) && constructor.getParameterTypes().length == 0) {
                    constructorToUse = constructor;
                }
                Annotation[] annotations = constructor.getDeclaredAnnotations();
                for (Annotation annotation : annotations) {
                    if (annotation.annotationType() == Inject.class) {
                        amount++;
                        constructorToUse = constructor;
                    }
                }
            }
            if (amount > 1) {
                lock.unlock();
                throw new TooManyConstructorsException(String.format("There is too many constructors with annotation %s in class %s", Inject.class, tClass));
            }
            if (Objects.isNull(constructorToUse)) {
                lock.unlock();
                throw new ConstructorNotFoundException(String.format("There is no constructors with annotation %s in class %s", Inject.class, tClass));
            }

            Class<?>[] args = constructorToUse.getParameterTypes();
            Object[] initArgs = new Object[args.length];

            for (int i = 0; i < args.length; i++) {
                Class<?> arg = args[i];
                initArgs[i] = getProvider(arg).getInstance();
            }
            T t;

            if (args.length == 0) {
                t = (T) constructorToUse.newInstance();
            } else {
                t = (T) constructorToUse.newInstance(initArgs);
            }
            return t;
        }
    }

    /**
     * {@inheritDoc}
     */
    public <T> void bind(Class<T> interfaceClass, Class<? extends T> impl) {
        availableClasses.put(interfaceClass, impl);
    }

    /**
     * {@inheritDoc}
     */
    public <T> void bindSingleton(Class<T> interfaceClass, Class<? extends T> impl) {
        singletons.put(interfaceClass, new AbstractMap.SimpleEntry<>(impl, null));
    }
}
