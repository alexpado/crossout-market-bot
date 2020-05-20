package fr.alexpado.bots.cmb.tools.section;

import java.util.ArrayList;
import java.util.List;

public class AdvancedHelpBuilder {

    private final List<AdvancedHelpSection> sections = new ArrayList<>();
    private String description;

    public void setDescription(String description) {
        this.description = description;
    }

    public void addSection(AdvancedHelpSection section) {
        this.sections.add(section);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(this.description).append("\n\n");
        this.sections.forEach(section -> builder.append(section.toString()));
        return builder.toString();
    }

}
