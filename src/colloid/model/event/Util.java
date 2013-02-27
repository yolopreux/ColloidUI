package colloid.model.event;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import colloid.model.IRecount;
import colloid.model.LogUtil;

public class Util {

    public static File[] filesByPath(String path) {
        File dir = new File(path);

        return dir.listFiles();
    }

    public static Date parseDate(String line) {
        final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss.S",
                Locale.getDefault());
        Date logTime;
        try {
            logTime = timeFormat.parse(line.substring(1, 13));
            return logTime;
        } catch (ParseException e) {
            System.out.println(e.getMessage());
        } catch (StringIndexOutOfBoundsException e) {
            System.out.println(e);
        }

        return null;
    }

    /**
     * Compare time
     * @param logtime
     * @param lastLine
     * @return
     */
    public static boolean isDone(Date logtime, String lastLine) {
        if (logtime.getTime() > parseDate(lastLine).getTime()) {
            return false;
        }

        return true;
    }
}
