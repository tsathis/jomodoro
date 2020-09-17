package com.github.tharindusathis.jomodoro.timer;

public final class Configs
{
    private static int timerDuration = 25 * 60;
    private static int breakTimerDuration = 5 * 60;
    private static int flashNotifyInterval = 60;

    public static int getTimerDuration()
    {
        return timerDuration;
    }

    public static void setTimerDuration( int timerDuration )
    {
        Configs.timerDuration = timerDuration;
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
