package xo.marketbot.library.services.commands.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation use to mark a method as candidate for handling a command.
 *
 * @author alexpado
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Command {

    /**
     * Syntax string that will be used to trigger the annotated method.
     *
     * @return A syntax string
     */
    String value();

}
