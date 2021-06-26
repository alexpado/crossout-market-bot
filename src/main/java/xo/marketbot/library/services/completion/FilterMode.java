package xo.marketbot.library.services.completion;

import org.jetbrains.annotations.NotNull;
import xo.marketbot.library.services.commands.interfaces.IFilterMode;

public enum FilterMode implements IFilterMode {

    STRICT(true),
    STARTING_WITH(false);


    final boolean strict;

    FilterMode(boolean strict) {

        this.strict = strict;
    }

    @Override
    public boolean isStrict() {

        return this.strict;
    }

    @Override
    public boolean isMatching(@NotNull String strA, @NotNull String strB) {

        if (this.isStrict()) {
            return strA.equalsIgnoreCase(strB);
        } else {
            return strA.toLowerCase().startsWith(strB.toLowerCase());
        }
    }

}
