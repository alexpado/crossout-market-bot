package fr.alexpado.bots.cmb.tools;

import fr.alexpado.bots.cmb.libs.jda.events.CommandEvent;

import java.util.HashMap;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandArgumentsParser {

    private final HashMap<String, String> arguments = new HashMap<>();

    public CommandArgumentsParser(CommandEvent event) {
        this(String.join(" ", event.getArgs()));
    }

    public CommandArgumentsParser(String fullCommand) {
        Pattern pattern = Pattern.compile("--(\\S*) (\"([^\"]*)\"|[^-]\\S*)?");
        Matcher matcher = pattern.matcher(fullCommand);
        System.out.println(matcher.groupCount());

        while (matcher.find()) {

            String argName = matcher.group(1);
            String argQuotedValue = matcher.group(2);
            String argValue = matcher.group(3);

            if (argValue == null) {
                this.arguments.put(argName, argQuotedValue);
            } else if (argQuotedValue == null) {
                this.arguments.put(argName, "");
            } else {
                this.arguments.put(argName, argQuotedValue);
            }
        }
    }

    public Optional<String> get(String arg) {
        return Optional.ofNullable(this.arguments.getOrDefault(arg, null));
    }

}
