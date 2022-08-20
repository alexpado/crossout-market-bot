package xo.marketbot.tools;

import fr.alexpado.xodb4j.interfaces.common.Nameable;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class SearchHelper {

    public static <T extends Nameable> Optional<T> search(List<T> elements, @Nullable String search) {

        if (elements.size() == 1) {
            return Optional.of(elements.get(0));
        }

        if (search == null) {
            return Optional.empty();
        }

        List<T> nameEquals = elements.stream()
                .filter(el -> el.getName().equalsIgnoreCase(search)).toList();

        if (nameEquals.size() == 1) {
            return Optional.of(nameEquals.get(0));
        }

        String lcSearch = search.toLowerCase();
        List<T> startingWith = elements.stream()
                .filter(el -> el.getName().toLowerCase().startsWith(lcSearch)).toList();

        if (startingWith.size() == 1) {
            return Optional.of(startingWith.get(0));
        }

        return Optional.empty();
    }

}
