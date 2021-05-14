package refl;

import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Stores packages that search for classes whose
 * instances will be entered in
 * the fields marked with the Auto Injectable annotation.
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Configuration {
    String[] packages();
}
