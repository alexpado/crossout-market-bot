package xo.marketbot.library.services.completion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class StringScanner {

    private static final String DELIMITER = "\"";

    private final List<String> input;

    public StringScanner(String string) {

        this.input = Arrays.asList(string.split(" "));
    }

    public StringScanner(List<String> input) {

        this.input = input;
    }

    public void scan(Consumer<String> onFlag, BiConsumer<String, String> onArgument, Consumer<String> onInput) {

        for (int i = 0 ; i < this.input.size() ; i++) {

            String item = this.input.get(i);

            if (item.startsWith("--")) {

                String name = item.substring(2);
                String value;

                i++;

                if (this.input.size() <= i) {
                    // Ignore
                    continue;
                }

                String next = this.input.get(i);

                if (next.startsWith(DELIMITER)) {
                    List<String> valueArgs = new ArrayList<>();

                    while (this.input.size() > i && !next.endsWith(DELIMITER)) {
                        valueArgs.add(next);
                        i++;
                        if (this.input.size() > i) {
                            next = this.input.get(i);
                        }
                    }
                    valueArgs.add(next);

                    String content = String.join(" ", valueArgs);
                    value = content.substring(1, content.length() - 1);
                } else {
                    value = next;
                }

                onArgument.accept(name, value);
            } else if (item.startsWith("-")) {
                onFlag.accept(item.substring(1));
            } else {
                onInput.accept(item);
            }
        }

    }
}
