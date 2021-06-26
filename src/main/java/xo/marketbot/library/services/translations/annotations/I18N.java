package xo.marketbot.library.services.translations.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used to annotate a field subject to internationalization.
 *
 * @author alexpado
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface I18N {

    /**
     * The translation key to use when translating the field annotated.
     *
     * @return The translation key.
     */
    String value() default "";

}
