package scoreboard;

public class Clock {
    private int minutes, seconds, period;
    boolean isIntermission = true;
    final private int ENDING_PERIOD, MINUTES_IN_PERIOD, MINUTES_IN_OVERTIME, MINUTES_IN_INTERMISSION, MINUTES_IN_INTERMISSION_BEFORE_OVERTIME;

    public Clock(int sportId) {
        switch (sportId) {
            case 1:
                ENDING_PERIOD = 3;
                MINUTES_IN_PERIOD = 20;
                MINUTES_IN_OVERTIME = 5;
                MINUTES_IN_INTERMISSION = 20;
                MINUTES_IN_INTERMISSION_BEFORE_OVERTIME = 5;
                break;
            default:
                ENDING_PERIOD = 0;
                MINUTES_IN_PERIOD = 0;
                MINUTES_IN_OVERTIME = 0;
                MINUTES_IN_INTERMISSION = 0;
                MINUTES_IN_INTERMISSION_BEFORE_OVERTIME = 0;
                break;
        }
        period = 1;
    }

    public void reset() {
        seconds = 0;
        if (period <= ENDING_PERIOD) {
            minutes = isIntermission ? MINUTES_IN_INTERMISSION : MINUTES_IN_PERIOD;
        } else {
            minutes = isIntermission ? MINUTES_IN_OVERTIME : MINUTES_IN_INTERMISSION_BEFORE_OVERTIME;
        }
    }

    public void tickDown() {
        seconds--;
        if (seconds == -1) {
            minutes--;
            seconds = 59;
        }
    }

    public void tickUp() {
        seconds--;
        if (seconds == 60) {
            minutes--;
            seconds = 0;
        }
    }

    public boolean isExpired() {
        return minutes == 0 && seconds == 0;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public boolean isIntermission() {
        return isIntermission;
    }

    public void setIntermission(boolean isIntermission) {
        this.isIntermission = isIntermission;
    }

    public int getENDING_PERIOD() {
        return ENDING_PERIOD;
    }

    public int getMINUTES_IN_PERIOD() {
        return MINUTES_IN_PERIOD;
    }

    public int getMINUTES_IN_OVERTIME() {
        return MINUTES_IN_OVERTIME;
    }

    public int getMINUTES_IN_INTERMISSION() {
        return MINUTES_IN_INTERMISSION;
    }

    public int getMINUTES_IN_INTERMISSION_BEFORE_OVERTIME() {
        return MINUTES_IN_INTERMISSION_BEFORE_OVERTIME;
    }
}
