package me.blueslime.supervotes.modules.event.timer;

public class Time {
    private final long system;
    private final long ms;


    private Time(long system, String format, int value) {
        this.system = system;
        this.ms = value * TimeUnit.fromString(format).getMilliseconds();
    }

    private Time(String format, int value) {
        this(0, format, value);
    }

    private Time(long system, Time builder) {
        this.system = system;
        this.ms = builder.getMilliseconds();
    }

    public static Time builder(String format, int value) {
        return new Time(format, value);
    }

    public static Time builder(long current, String format, int value) {
        return new Time(current, format, value);
    }

    public static Time builder(long current, Time builder) {
        return new Time(current, builder);
    }

    public long getConvertedMilliseconds() {
        return system + ms;
    }

    public long getMilliseconds() {
        return ms;
    }

    public boolean reached() {
        return (getConvertedMilliseconds() - System.currentTimeMillis()) < 0;
    }
}
