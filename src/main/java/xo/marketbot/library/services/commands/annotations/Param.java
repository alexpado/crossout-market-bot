package xo.marketbot.library.services.commands.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used to mark a method's parameter as candidate to receive a command parameter.
 *
 * @author alexpado
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface Param {

    /**
     * The name of the parameter used in the syntax string.
     *
     * @return Variable name
     */
    String value() default "";

}
