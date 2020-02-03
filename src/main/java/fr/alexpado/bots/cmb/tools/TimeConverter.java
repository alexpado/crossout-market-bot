package fr.alexpado.bots.cmb.tools;

import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimeConverter {

    private final long hours;
    private long minutes;
    private long seconds;

    public TimeConverter(long duration) {
        this.hours   = TimeUnit.HOURS.convert(duration, TimeUnit.SECONDS);
        this.minutes = TimeUnit.MINUTES.convert(duration, TimeUnit.SECONDS);
        this.seconds = duration;
        this.minutes -= this.hours * 60;
        this.seconds -= (this.hours * 3600) + (this.minutes * 60);
    }

    public static long fromString(String str) {
        String  regex   = "(([0-5]?[0-9])h)?(([0-5]?[0-9])m?)?";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        long    time    = 0L;
        if (matcher.find()) {
            String hour   = matcher.group(2);
            String minute = matcher.group(4);
            if (hour != null) {
                time += Long.parseLong(hour) * 3600000;
            }
            if (minute != null) {
                time += Long.parseLong(minute) * 60000;
            }
        }
        return time;
    }

    @Override
    public String toString() {
        // TODO remove 0 values
        if (this.hours != 0) {
            return String.format("%02dh%02dm%02ds", hours, minutes, seconds);
        } else if (this.minutes != 0) {
            return String.format("%02dm%02ds", minutes, seconds);
        }
        return String.format("%02ds", seconds);
    }

}
