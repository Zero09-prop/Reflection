package refl;

import org.reflections.Reflections;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


/**
 * Provides Dependency Injection.
 */
public class Inject {
    private List<Object> list = new ArrayList<>();

    /**
     * Inject dependencies to the specified object.
     *
     * @param object object for inject dependencies
     * @param <T>    the type of the class
     * @return object  with injected dependencies
     * @throws InjectorException if the application does not have access to the definition of the specified class,
     *                           field, method, or constructor, or an error to embed in a single object.
     */
    public static <T> T inject(T object) throws InjectorException {

        List<Object> classInject = new ArrayList<>();
        Class<T> clazz = (Class<T>) object.getClass();
        Field[] fields = clazz.getDeclaredFields();

        ArrayList<Object> objects = new ArrayList<>();

        for (Field f : fields) {
            if (f.isAnnotationPresent(AutoInjectable.class)) {
                f.setAccessible(true);
                if (f.getType().getName().contains("java.util.List")) {

                    ParameterizedType fieldListType = (ParameterizedType) f.getGenericType();
                    Class<?> fieldGenericType = (Class<?>) fieldListType.getActualTypeArguments()[0];
                    getClasses(objects, fieldGenericType);

                    for (Object o : objects) {
                        if (o != null && fieldGenericType.isAssignableFrom(o.getClass())) {
                            classInject.add(o);
                        }

                        try {
                            f.set(object, classInject);
                        } catch (IllegalAccessException e) {

                            throw new InjectorException(e);
                        }
                    }
                } else {

                    getClasses(objects, f.getType());
                    for (Object o : objects) {
                        if (o != null && f.getType().isAssignableFrom(o.getClass())) {
                            classInject.add(o);
                        }

                        if (classInject.size() == 1) {
                            try {
                                f.set(object, classInject.get(0));
                            } catch (IllegalAccessException e) {

                                throw new InjectorException(e);
                            }
                        } else {

                            throw new InjectorException("The number of classes is more than 1 (only 1 is allowed)");
                        }
                    }
                }
            }
        }

        return object;
    }


    private static void getClasses(ArrayList<Object> objects, Class<?> type) {

        Configuration packages = Inject.class.getAnnotation(Configuration.class);
        Reflections reflections;
        for (String pack : packages.packages()) {
            reflections = new Reflections(pack);
            Set<Class<?>> classes = reflections.getSubTypesOf((Class<Object>) type);
            for (Class<?> o : classes) {
                try {
                    objects.add(o.newInstance());
                } catch (InstantiationException | IllegalAccessException e) {

                }
            }
        }

    }
}

