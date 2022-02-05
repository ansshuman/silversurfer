package time;

/*
    Author : Anshuman Singh
    Email : ansshu.sin@gmail.com
 */

import static time.util.Constants.*;

import java.util.Calendar;
import java.util.Date;

public abstract class TimeSurfer {

    public final Date start;
    public final Date end;
    public final Long duration;
    public final Long nonWeekendTime;
    public final Long workingTime;
    public final int work_day_start_hour;
    public final int work_day_end_hour;
    public final int work_day_start_min;
    public final int work_day_end_min;
    public final int work_day_start_sec;
    public final int work_day_end_sec;
    public final int work_day_start_ms;
    public final int work_day_end_ms;
    public final long work_day_start_time;
    public final long work_day_end_time;

    /*
        Constructor
     */
    public TimeSurfer(Date start, Date end) {

        if (start.compareTo(end) > 0) {
            this.start = end;
            this.end = start;
        } else {
            this.start = start;
            this.end = end;
        }

        // default work day start at 9AM
        this.work_day_start_hour = 9;
        this.work_day_start_min = 0;
        this.work_day_start_sec = 0;
        this.work_day_start_ms = 0;

        // default work day end at 5PM
        this.work_day_end_hour = 17;
        this.work_day_end_min = 0;
        this.work_day_end_sec = 0;
        this.work_day_end_ms = 0;

        this.work_day_start_time = this.work_day_start_hour * MILLIS_IN_HOUR;
        this.work_day_end_time = this.work_day_end_hour * MILLIS_IN_HOUR;

        this.duration = this.end.getTime() - this.start.getTime();
        this.nonWeekendTime = calculateNonWeekendTimeInMillis(this.start, this.end);
        this.workingTime = calculateWorkingTimeInMillis(this.start, this.end);

    }

    /*
        provide hh:mm:ss:mmm format time as start and end times of working period
        to be considered
     */
    public TimeSurfer(Date start, Date end, String work_day_start, String work_day_end) {

        if (start.compareTo(end) > 0) {
            this.start = end;
            this.end = start;
        } else {
            this.start = start;
            this.end = end;
        }

        String[] work_day_start_hms = work_day_start.split(":");
        String[] work_day_end_hms = work_day_end.split(":");

        this.work_day_start_hour = Integer.parseInt(work_day_start_hms[0]);
        this.work_day_start_min = Integer.parseInt(work_day_start_hms[1]);
        this.work_day_start_sec = Integer.parseInt(work_day_start_hms[2]);
        this.work_day_start_ms = Integer.parseInt(work_day_start_hms[3]);

        this.work_day_end_hour = Integer.parseInt(work_day_end_hms[0]);
        this.work_day_end_min = Integer.parseInt(work_day_end_hms[1]);
        this.work_day_end_sec = Integer.parseInt(work_day_end_hms[2]);
        this.work_day_end_ms = Integer.parseInt(work_day_end_hms[3]);

        this.work_day_start_time = this.work_day_start_hour * MILLIS_IN_HOUR + this.work_day_start_min * MILLIS_IN_MINUTE + this.work_day_start_sec * MILLIS_IN_SECOND + this.work_day_start_ms;

        this.work_day_end_time = this.work_day_end_hour * MILLIS_IN_HOUR + this.work_day_end_min * MILLIS_IN_MINUTE + this.work_day_end_sec * MILLIS_IN_SECOND + this.work_day_end_ms;

        this.duration = this.end.getTime() - this.start.getTime();
        this.nonWeekendTime = calculateNonWeekendTimeInMillis(this.start, this.end);
        this.workingTime = calculateWorkingTimeInMillis(this.start, this.end);

    }

    /*
        Returns total time in milliseconds between start and end date
        excluding time of weekends that is Saturday & Sunday
     */
    public Long calculateNonWeekendTimeInMillis(Date start, Date end) {

        int t = start.compareTo(end);
        if (t >= 0) {
            if (t == 0) return 0L;
            return -1L;
        }
        Calendar sCal = Calendar.getInstance(), eCal = Calendar.getInstance();
        sCal.setTime(start);
        eCal.setTime(end);
        boolean startIsSat = sCal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY,
                startIsSun = sCal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY;
        boolean endIsSat = eCal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY,
                endIsSun = eCal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY;

        if (startIsSat || startIsSun) {
            if (startIsSat) {
                addTimeToCal(sCal, 2 * MILLIS_IN_DAY);
            } else {
                addTimeToCal(sCal, MILLIS_IN_DAY);
            }
            sCal.set(Calendar.HOUR_OF_DAY, 0);
            sCal.set(Calendar.MINUTE, 0);
            sCal.set(Calendar.SECOND, 0);
            sCal.set(Calendar.MILLISECOND, 0);
        }

        if (endIsSat || endIsSun) {
            if (endIsSun) {
                addTimeToCal(eCal, -2 * MILLIS_IN_DAY);
            } else {
                addTimeToCal(eCal, -1 * MILLIS_IN_DAY);
            }
            eCal.set(Calendar.HOUR_OF_DAY, 23);
            eCal.set(Calendar.MINUTE, 59);
            eCal.set(Calendar.SECOND, 59);
            eCal.set(Calendar.MILLISECOND, 999);
        }

        long dur = eCal.getTimeInMillis() - sCal.getTimeInMillis();
        if (dur <= 0) return 0L;
        long wt = 0, numWeeks = dur / (MILLIS_IN_WEEK);
        wt += numWeeks * MILLIS_IN_WEEK;
        addTimeToCal(sCal, wt);
        dur -= wt;
        wt *= 5;
        wt /= 7;
        wt += dur;
        while (sCal.getTimeInMillis() <= eCal.getTimeInMillis()) {
            if (sCal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
                wt -= 2 * MILLIS_IN_DAY;
                break;
            }
            addTimeToCal(sCal, MILLIS_IN_DAY);
        }

        if (endIsSat || endIsSun) wt += 1;
        return wt;

    }

    /*
        Returns total time in milliseconds between start and end date
        excluding time of weekends that is Saturday & Sunday, and non-working hours
        Defaults working hours are 9AM to 5PM
     */
    public Long calculateWorkingTimeInMillis(Date start, Date end) {

        int t = start.compareTo(end);
        if (t >= 0) {
            if (t == 0) return 0L;
            return -1L;
        }
        Calendar sCal = Calendar.getInstance(), eCal = Calendar.getInstance();
        sCal.setTime(start);
        eCal.setTime(end);

        long sCalDayTime = getDayTime(sCal), eCalDayTime = getDayTime(eCal);

        if (sCalDayTime < work_day_start_time || sCalDayTime > work_day_end_time) {
            if (sCalDayTime > work_day_end_time) {
                addTimeToCal(sCal, MILLIS_IN_DAY);
            }
            sCal.set(Calendar.HOUR_OF_DAY, work_day_start_hour);
            sCal.set(Calendar.MINUTE, work_day_start_min);
            sCal.set(Calendar.SECOND, work_day_start_sec);
            sCal.set(Calendar.MILLISECOND, work_day_start_ms);
        }

        if (eCalDayTime < work_day_start_time || eCalDayTime > work_day_end_time) {
            if (eCalDayTime < work_day_start_time) {
                addTimeToCal(eCal, -1 * MILLIS_IN_DAY);
            }
            eCal.set(Calendar.HOUR_OF_DAY, work_day_end_hour);
            eCal.set(Calendar.MINUTE, work_day_end_min);
            eCal.set(Calendar.SECOND, work_day_end_sec);
            eCal.set(Calendar.MILLISECOND, work_day_end_ms);
        }

        if (sCal.getTime().compareTo(eCal.getTime()) >= 0) {
            return 0L;
        }

        boolean startIsSat = sCal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY,
                startIsSun = sCal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY;
        boolean endIsSat = eCal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY,
                endIsSun = eCal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY;

        if (startIsSat || startIsSun) {
            if (startIsSat) {
                addTimeToCal(sCal, 2 * MILLIS_IN_DAY);
            } else {
                addTimeToCal(sCal, MILLIS_IN_DAY);
            }
            sCal.set(Calendar.HOUR_OF_DAY, work_day_start_hour);
            sCal.set(Calendar.MINUTE, work_day_start_min);
            sCal.set(Calendar.SECOND, work_day_start_sec);
            sCal.set(Calendar.MILLISECOND, work_day_start_ms);
        }

        if (endIsSat || endIsSun) {
            if (endIsSun) {
                addTimeToCal(eCal, -2 * MILLIS_IN_DAY);
            } else {
                addTimeToCal(eCal, -1 * MILLIS_IN_DAY);
            }
            eCal.set(Calendar.HOUR_OF_DAY, work_day_end_hour);
            eCal.set(Calendar.MINUTE, work_day_end_min);
            eCal.set(Calendar.SECOND, work_day_end_sec);
            eCal.set(Calendar.MILLISECOND, work_day_end_ms);
        }

        if (sCal.getTime().compareTo(eCal.getTime()) >= 0) return 0L;

        long non_weekend_time = calculateNonWeekendTimeInMillis(sCal.getTime(), eCal.getTime());
        long non_weekend_days = non_weekend_time / MILLIS_IN_DAY, rem = non_weekend_time % MILLIS_IN_DAY;
        long res = (non_weekend_days * MILLIS_IN_DAY * (work_day_end_time - work_day_start_time)) / MILLIS_IN_DAY;

        if (getDayTime(sCal) <= getDayTime(eCal)) {
            return res + rem;
        }

        return res + rem - ((MILLIS_IN_DAY - work_day_end_time) + work_day_start_time);

    }

    /*
        Shifts calendar time by specific amount of time in milliseconds
     */
    public void addTimeToCal(Calendar cal, long time) {

        long calTime = cal.getTimeInMillis();
        calTime += time;
        cal.setTime(new Date(calTime));

    }

    public Long getDayTime(Calendar cal) {

        Calendar nCal = Calendar.getInstance();
        nCal.setTime(cal.getTime());
        nCal.set(Calendar.HOUR_OF_DAY, 0);
        nCal.set(Calendar.MINUTE, 0);
        nCal.set(Calendar.SECOND, 0);
        nCal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis() - nCal.getTimeInMillis();

    }

    public String toString() {

        return "Start Date: " + this.start.toString() + ", " +
                "End Date: " + this.end.toString() + ", " +
                "Duration: " + getHMS(this.duration) + ", " +
                "Non Weekend Time: " + getHMS(this.nonWeekendTime) + ", " +
                "Working Time: " + getHMS(this.workingTime);

    }

    /*
        Returns equivalent 'day hour min sec millisecond' representation of duration
     */
    public String getHMS(long milliseconds) {

        long millis = milliseconds;
        long days = millis / MILLIS_IN_DAY;
        millis = millis % MILLIS_IN_DAY;
        long hours = millis / (MILLIS_IN_HOUR);
        millis = millis % (MILLIS_IN_HOUR);
        long min = millis / (MILLIS_IN_MINUTE);
        millis = millis % (MILLIS_IN_MINUTE);
        long secs = millis / (MILLIS_IN_SECOND);
        millis = millis % (MILLIS_IN_SECOND);

        return days + "d " + hours + "h " + min + "m " + secs + "s " + millis + "ms";

    }
}
