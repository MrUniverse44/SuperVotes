package me.blueslime.supervotes.modules.event.timer;

import java.util.Locale;

public enum TimeUnit {
    DAY(86400000),
    HOUR(3600000),
    MINUTE(60000),
    SECOND(1000);

    private final long milliseconds;

    TimeUnit(long milliseconds) {
        this.milliseconds=milliseconds;
    }

    public long getMilliseconds() {
        return milliseconds;
    }

    public static TimeUnit fromString(String value){
        switch (value.toLowerCase(Locale.ENGLISH)) {
            case "day":
            case "days":
            case "dia":
            case "d":
                return DAY;
            case "hora":
            case "hour":
            case "hours":
            case "h":
                return HOUR;
            case "minutes":
            case "minutos":
            case "minute":
            case "minuto":
            case "min":
            case "m":
                return MINUTE;
            default:
            case "seconds":
            case "second":
            case "s":
            case "segundos":
            case "segundo":
                return SECOND;
        }
    }
}

