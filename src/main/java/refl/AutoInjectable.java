package refl;


import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;



/**
 * Specifies which field you will need to implement dependency injection.
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface AutoInjectable {
    Class clazz() default Object.class;
}
