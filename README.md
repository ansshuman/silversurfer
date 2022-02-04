# silversurfer
A store for programs that I went out looking for on the web, and couldn't find any substantial help (couldn't find a source to copy 🙃). The repo exists to make life easier for software developers who are on quest for similar software...

Currently the repo hosts only one program, written in Java: src/com.silversurfer.time.util.TimeSurfer is a helper abstract class that one can extend to modify as per ones need.

## TimeSurfer
TimeSurfer is capable of calculating working time between two dates down to the millisecond.
Working time between two dates means sum of all time periods in between the two instances, which were not a weekend (Saturday or Sunday), and were inside custom set working hours (default being start: 9AM, end: 5PM).
Needless to say it can also just provide the non-weekend duration between the two date instances.

An example on how to use TimeSurfer is shown in src/Main

The best part is, it is O(1) in time and space complexity both, no matter how big the period in between the two dates. 
