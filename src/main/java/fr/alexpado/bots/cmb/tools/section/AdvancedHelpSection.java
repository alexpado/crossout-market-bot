package fr.alexpado.bots.cmb.tools.section;

import java.util.HashMap;

public class AdvancedHelpSection {

    private final String sectionName;

    private final HashMap<String, String> fields = new HashMap<>();

    public AdvancedHelpSection(String sectionName) {
        this.sectionName = sectionName;
    }

    public void addField(String key, String value) {
        this.fields.put(key, value);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append("** # ").append(this.sectionName).append("**\n");
        builder.append("====================\n\n");

        this.fields.forEach((k, v) -> {
            builder.append(k).append("\n");
            builder.append(v).append("\n\n");
        });

        builder.append("\n");

        return builder.toString();
    }

}
