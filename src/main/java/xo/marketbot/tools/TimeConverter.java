package xo.marketbot.tools;

import xo.marketbot.exceptions.EmbedException;

import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimeConverter {

    private final long hours;
    private       long minutes;
    private       long seconds;

    public TimeConverter(long duration) {

        this.hours   = TimeUnit.HOURS.convert(duration, TimeUnit.SECONDS);
        this.minutes = TimeUnit.MINUTES.convert(duration, TimeUnit.SECONDS);
        this.seconds = duration;
        this.minutes -= this.hours * 60;
        this.seconds -= (this.hours * 3600) + (this.minutes * 60);
    }

    public static long fromString(String str) {

        try {
            String  regex   = "(?<hours>\\d*h)?(?<minutes>\\d+m)?";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(str);
            long    time    = 0L;
            if (matcher.find()) {
                String hour   = matcher.group("hours");
                String minute = matcher.group("minutes");
                if (hour != null) {
                    time += Long.parseLong(hour.replace("h", "")) * 3600000;
                }
                if (minute != null) {
                    time += Long.parseLong(minute.replace("m", "")) * 60000;
                }
            }
            return time;
        } catch (NumberFormatException e) {
            throw new EmbedException("Please input a valid frequency");
        }
    }

    @Override
    public String toString() {

        if (this.hours != 0) {
            if (this.seconds != 0) {
                return String.format("%sh%02dm%02ds", this.hours, this.minutes, this.seconds);
            } else if (this.minutes != 0) {
                return String.format("%sh%02dm", this.hours, this.minutes);
            } else {
                return String.format("%sh", this.hours);
            }
        } else if (this.minutes != 0) {
            if (this.seconds != 0) {
                return String.format("%02dm%02ds", this.minutes, this.seconds);
            }
            return String.format("%sm", this.minutes);
        }
        return String.format("%02ds", this.seconds);
    }

}
