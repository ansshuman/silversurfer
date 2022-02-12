# silversurfer

A store for programs that I went out looking for on the web, and couldn't find any substantial help (couldn't find a source to copy 🙃). The repo exists to make life easier for software developers who are on quest for similar software...

Currently, the repo hosts only one program, written in Java: src/com.github.ansshuman.timesurfer.TimeSurfer. It is a helper abstract class that one can extend to modify as per one's needs.

## TimeSurfer

TimeSurfer is capable of calculating working time between two dates down to the millisecond.

Working time between two dates means sum of all time periods in between the two date instances, which were not a weekend (Saturday or Sunday), and were inside custom set working hours (default being start: 9AM, end: 5PM).

Needless to say, it can also just provide the non-weekend duration between the two date instances. An example on how to use TimeSurfer is shown in src/Main

___The best part is, it is O(1) in time and space complexity both, no matter how big the period in between the two dates.___

#### How does TimeSurfer work?

The logic for calculation of non weekend time between two dates:

    1) Check if start == end if yes return 0, if start > end return -1, or switch the dates, whatever you want.

    2) Check if start date falls on a weekend (Sat or Sun), if it does, set start to the next monday 00:00:00 AM.

    3) Check if end date falls on a weekend, if it does, set end date to that weekends Saturday 00:00:00 AM.
       
       Now both start and end don't fall on weekends (well end date might but its on start of Saturday),
       and non weekend time remains same between start and end. Basically trim out useless time at start and end.

    4) Check if start has become greater than end (happens if both fell on same weekend), if yes, return 0.

    5) Calculate time duration in milliseconds between these two dates.

    6) Calculate number of whole weeks in this duration by dividing it by milliseconds in a week, let it be n.

    7) Out of all time in these n weeks, 5/7th of it is the total working time in these n weeks,
       let this be 'FirstDuration'.

    8) Advance start date by these n weeks, the weekday will not change for start hence start will still be a working
       day and time difference between start and end will now be less than 7 days,
       let this difference be 'SecondDuration'.

    9) Now simply iterate through remaining days greater than start and less than end, checking if any of them is
       Saturday. If yes, then Sat and Sun both exist between start and end as both are working days.
       Hence return working time as Firstduration + SecondDuration - (time in two days (Sat and Sun))
       Otherwise just return Firstduration + SecondDuration

The logic for calculation of working time between two dates:

    1) Same as that for non weekend calculation check if start and end are valid.
    
    2) Check if start is out of working hours, if yes shift start to next point of starting of working hours.

    3) Check if end is out of working hours, if yes shift end to previous point of ending of working hours.

    4) Same as for non-weekend time, if start is on weekend shift start time to next Monday's start of working hours.
       Similarly for end, if it's on a weekend shift end to previous Friday's end of working hours.

       Now both start and end are on weekdays (non-weekend) and within working hours.

    5) Check if start has become greater than end (happens if both fell on same weekend), if yes, return 0.

    6) Calculate total non-weekend time between these start and end dates using the method described above.

    7) Calculate whole working days between these two by dividing value obtained in above step by total time in a day,
       Out of these days time, (total working time in a day)/(time in a day) fraction of it is total working time in these days.
       Let that fraction be FirstDuration, and the remainder of division of non weekend time by one days time be SecondDuration.

    8) Check if time between 00:00:00 AM on start date, to start date, is greater than total time between
       00:00:00 AM on end date, to end date. Basically whether more time has passed on start dates day than on ends or not.
       
    9) If above check is yes then duration remaining after removing the whole non-weekend days contains also the non working time of one day,
       It's not obvious but try thinking on it and you will find it to be true. 
       Hence return FirstDuration + SecondDuration - (Non working time in a day).
       Otherwise return FirstDuration + SecondDuration.

In the future, if I get time, will try to exclude out a list of holidays apart from weekends too. Or maybe, you can do that and make a pull request? 😬 

###### As usual, coder discretion is advised when using code procured from the web. 
