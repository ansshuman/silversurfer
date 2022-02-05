package time;

/*
    Author : Anshuman Singh
    Email : ansshu.sin@gmail.com
 */

import java.util.Calendar;
import java.util.Date;

import static time.util.Constants.*;

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

    // default constructor, used to get object to access methods
    public TimeSurfer() {
        this.start = new Date(0);
        this.end = new Date(0);

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

        this.work_day_start_time = this.work_day_start_hour * HOUR_IN_MILLIS;
        this.work_day_end_time = this.work_day_end_hour * HOUR_IN_MILLIS;

        this.duration = 0L;
        this.nonWeekendTime = 0L;
        this.workingTime = 0L;
    }

    // Constructor for obtaining parameters within two dates with default working hours
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

        this.work_day_start_time = this.work_day_start_hour * HOUR_IN_MILLIS;
        this.work_day_end_time = this.work_day_end_hour * HOUR_IN_MILLIS;

        this.duration = this.end.getTime() - this.start.getTime();
        this.nonWeekendTime = calculateNonWeekendTimeInMillis(this.start, this.end);
        this.workingTime = calculateWorkingTimeInMillis(this.start, this.end);

    }

    /*
        constructor for custom working hours, used to get object to access methods with custom working hours
        provide start and end times of working time in a day as hh:mm:ss:mmm (24, hour format) string
     */
    public TimeSurfer(String work_day_start, String work_day_end) {
        this.start = new Date(0);
        this.end = new Date(0);

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

        this.work_day_start_time = this.work_day_start_hour * HOUR_IN_MILLIS + this.work_day_start_min * MINUTE_IN_MILLIS + this.work_day_start_sec * SECOND_IN_MILLIS + this.work_day_start_ms;

        this.work_day_end_time = this.work_day_end_hour * HOUR_IN_MILLIS + this.work_day_end_min * MINUTE_IN_MILLIS + this.work_day_end_sec * SECOND_IN_MILLIS + this.work_day_end_ms;

        this.duration = 0L;
        this.nonWeekendTime = 0L;
        this.workingTime = 0L;
    }

    /*
        Constructor for obtaining parameters within two dates with custom working hours
        provide start and end times of working time in a day as hh:mm:ss:mmm (24, hour format) string
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

        this.work_day_start_time = this.work_day_start_hour * HOUR_IN_MILLIS + this.work_day_start_min * MINUTE_IN_MILLIS + this.work_day_start_sec * SECOND_IN_MILLIS + this.work_day_start_ms;

        this.work_day_end_time = this.work_day_end_hour * HOUR_IN_MILLIS + this.work_day_end_min * MINUTE_IN_MILLIS + this.work_day_end_sec * SECOND_IN_MILLIS + this.work_day_end_ms;

        this.duration = this.end.getTime() - this.start.getTime();
        this.nonWeekendTime = calculateNonWeekendTimeInMillis(this.start, this.end);
        this.workingTime = calculateWorkingTimeInMillis(this.start, this.end);

    }

    /*
        Returns total time in milliseconds between start and end date
        excluding time of weekends that is Saturday & Sunday
     */
    public Long calculateNonWeekendTimeInMillis(Date start, Date end) {

        int check = start.compareTo(end);
        if (check >= 0) {
            if (check == 0) return 0L;
            return -1L;
        }

        // Create calendar instances with the two dates
        Calendar startCal = Calendar.getInstance(), endCal = Calendar.getInstance();
        startCal.setTime(start);
        endCal.setTime(end);

        boolean startIsSat = startCal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY,
                startIsSun = startCal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY;

        boolean endIsSat = endCal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY,
                endIsSun = endCal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY;

        // if start falls on a weekend set startCal's time to next Monday 0 time
        if (startIsSat || startIsSun) {
            if (startIsSat) {
                addTimeToCal(startCal, 2 * DAY_IN_MILLIS);
            } else {
                addTimeToCal(startCal, DAY_IN_MILLIS);
            }
            startCal.set(Calendar.HOUR_OF_DAY, 0);
            startCal.set(Calendar.MINUTE, 0);
            startCal.set(Calendar.SECOND, 0);
            startCal.set(Calendar.MILLISECOND, 0);
        }

        // if end falls on a weekend set endCal's time to that Saturday 0 time
        if (endIsSat || endIsSun) {
            if (endIsSun) {
                addTimeToCal(endCal, -1 * DAY_IN_MILLIS);
            }
            endCal.set(Calendar.HOUR_OF_DAY, 0);
            endCal.set(Calendar.MINUTE, 0);
            endCal.set(Calendar.SECOND, 0);
            endCal.set(Calendar.MILLISECOND, 0);
        }

        long duration = endCal.getTimeInMillis() - startCal.getTimeInMillis();
        if (duration <= 0) return 0L; // return 0 if both start and end fell on same weekend

        long n_weeks = duration / WEEK_IN_MILLIS; // number of whole weeks in between startCal and endCal
        long rem_duration = duration % WEEK_IN_MILLIS; // remaining duration
        long n_weeks_time = n_weeks * WEEK_IN_MILLIS; // time in milliseconds for those weeks
        long n_weeks_non_weekend_time = 5 * (n_weeks_time / 7); // time in milliseconds for those weeks excluding weekends

        addTimeToCal(startCal, n_weeks_time); // advance startCal by those number of weeks

        /*
            check if a weekend exists between the now startCal and endCal, if yes, cut out 2 days worth of time
            otherwise return, time in milliseconds for those weeks excluding weekends, plus remaining duration
         */
        while (startCal.getTimeInMillis() < endCal.getTimeInMillis()) {

            if (startCal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY)
                return n_weeks_non_weekend_time + rem_duration - 2 * DAY_IN_MILLIS;

            addTimeToCal(startCal, DAY_IN_MILLIS);

        }

        return n_weeks_non_weekend_time + rem_duration;

    }

    /*
        Returns total time in milliseconds between start and end date
        excluding time of, weekends i.e. Saturday & Sunday and non-working hours
        Default working hours are 9AM to 5PM, construct object with custom working hours if needed
     */
    public Long calculateWorkingTimeInMillis(Date start, Date end) {

        int t = start.compareTo(end);
        if (t >= 0) {
            if (t == 0) return 0L;
            return -1L;
        }

        Calendar startCal = Calendar.getInstance(), endCal = Calendar.getInstance();
        startCal.setTime(start);
        endCal.setTime(end);

        long startCalDayTime = getDayTime(startCal), endCalDayTime = getDayTime(endCal);

        // if start not in working hours
        if (startCalDayTime < work_day_start_time || startCalDayTime > work_day_end_time) {
            // if start is after working hours, shift time to next day
            if (startCalDayTime > work_day_end_time) {
                addTimeToCal(startCal, DAY_IN_MILLIS);
            }
            // set start time to start of working hours
            startCal.set(Calendar.HOUR_OF_DAY, work_day_start_hour);
            startCal.set(Calendar.MINUTE, work_day_start_min);
            startCal.set(Calendar.SECOND, work_day_start_sec);
            startCal.set(Calendar.MILLISECOND, work_day_start_ms);
        }

        // if end not in working hours
        if (endCalDayTime < work_day_start_time || endCalDayTime > work_day_end_time) {
            // if end is before start of working hours, shift time to previous day
            if (endCalDayTime < work_day_start_time) {
                addTimeToCal(endCal, -1 * DAY_IN_MILLIS);
            }
            // set end time to end of working hours
            endCal.set(Calendar.HOUR_OF_DAY, work_day_end_hour);
            endCal.set(Calendar.MINUTE, work_day_end_min);
            endCal.set(Calendar.SECOND, work_day_end_sec);
            endCal.set(Calendar.MILLISECOND, work_day_end_ms);
        }

        // if both start and end fall in a continuous non-working period of time
        if (startCal.getTime().compareTo(endCal.getTime()) >= 0) {
            return 0L;
        }

        boolean startIsSat = startCal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY,
                startIsSun = startCal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY;

        boolean endIsSat = endCal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY,
                endIsSun = endCal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY;

        // if startCal falls on a weekend set startCal to next Mondays start of working hours
        if (startIsSat || startIsSun) {
            if (startIsSat) {
                addTimeToCal(startCal, 2 * DAY_IN_MILLIS);
            } else {
                addTimeToCal(startCal, DAY_IN_MILLIS);
            }
            startCal.set(Calendar.HOUR_OF_DAY, work_day_start_hour);
            startCal.set(Calendar.MINUTE, work_day_start_min);
            startCal.set(Calendar.SECOND, work_day_start_sec);
            startCal.set(Calendar.MILLISECOND, work_day_start_ms);
        }

        // if endCal falls on a weekend set endCal to Fridays end of working hours
        if (endIsSat || endIsSun) {
            if (endIsSun) {
                addTimeToCal(endCal, -2 * DAY_IN_MILLIS);
            } else {
                addTimeToCal(endCal, -1 * DAY_IN_MILLIS);
            }
            endCal.set(Calendar.HOUR_OF_DAY, work_day_end_hour);
            endCal.set(Calendar.MINUTE, work_day_end_min);
            endCal.set(Calendar.SECOND, work_day_end_sec);
            endCal.set(Calendar.MILLISECOND, work_day_end_ms);
        }

        //  if they ended up on same weekend return 0
        if (startCal.getTime().compareTo(endCal.getTime()) >= 0) return 0L;

        // now both startCal and endCal are on weekdays and within working hours
        long non_weekend_time = calculateNonWeekendTimeInMillis(startCal.getTime(), endCal.getTime());
        long non_weekend_days = non_weekend_time / DAY_IN_MILLIS, non_weekend_rem = non_weekend_time % DAY_IN_MILLIS;

        // for those days working time is: days * MILLIS_IN_DAY * ( (total working time in a day) / MILLIS_IN_DAY )
        long non_weekend_days_working_time = non_weekend_days * (work_day_end_time - work_day_start_time);

        // if start daytime is less than end daytime then non_weekend_rem doesn't contain non-working hours of a day
        if (getDayTime(startCal) <= getDayTime(endCal)) {
            return non_weekend_days_working_time + non_weekend_rem;
        }

        // cut off non-working time in a day as non_weekend_rem contains it
        long non_working_time_in_a_day = work_day_start_time + (DAY_IN_MILLIS - work_day_end_time);

        return non_weekend_days_working_time + non_weekend_rem - non_working_time_in_a_day;

    }

    // Shifts calendar time by specific amount of time in milliseconds
    public void addTimeToCal(Calendar cal, long time) {

        long calTime = cal.getTimeInMillis();
        long newTime = calTime + time;
        cal.setTime(new Date(newTime));

    }

    // returns time passed from the start of day on that Calendar day
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

    // returns equivalent 'day hour min sec millisecond' representation of duration provided
    public String getHMS(long milliseconds) {

        long millis = milliseconds;
        long days = millis / DAY_IN_MILLIS;
        millis = millis % DAY_IN_MILLIS;
        long hours = millis / HOUR_IN_MILLIS;
        millis = millis % HOUR_IN_MILLIS;
        long min = millis / MINUTE_IN_MILLIS;
        millis = millis % MINUTE_IN_MILLIS;
        long secs = millis / SECOND_IN_MILLIS;
        millis = millis % SECOND_IN_MILLIS;

        return days + "d " + hours + "h " + min + "m " + secs + "s " + millis + "ms";

    }
}
