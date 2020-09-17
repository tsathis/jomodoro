package com.github.tharindusathis.jomodoro.timer;

public final class Configs
{
    private static int defaultTimerDuration = 25 * 60;
    private static int breakTimerDuration = 5 * 60;
    private static int flashNotifyInterval = 60;

    public static int getDefaultTimerDuration()
    {
        return defaultTimerDuration;
    }

    public static void setDefaultTimerDuration( int defaultTimerDuration )
    {
        Configs.defaultTimerDuration = defaultTimerDuration;
    }

    public static int getBreakTimerDuration()
    {
        return breakTimerDuration;
    }

    public static void setBreakTimerDuration( int breakTimerDuration )
    {
        Configs.breakTimerDuration = breakTimerDuration;
    }

    public static int getFlashNotifyInterval()
    {
        return flashNotifyInterval;
    }

    public static void setFlashNotifyInterval( int flashNotifyInterval )
    {
        Configs.flashNotifyInterval = flashNotifyInterval;
    }

    private Configs(){}
}
