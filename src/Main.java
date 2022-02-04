import com.silversurfer.time.util.TimeSurfer;

import java.util.Date;
import static com.silversurfer.time.util.Constants.*;

public class Main {

    public static void main(String[] args) {

        Date end = new Date();
        Date start = new Date(end.getTime() - MILLIS_IN_DAY);
        TimeSurfer default_ts = new TimeSurfer(start, end) {};
        TimeSurfer ts = new TimeSurfer(start, end, "10:30:00:000", "18:00:00:000") {};
        System.out.println(default_ts + "\n" + ts);

    }

}
