package colloid.model.event;

import java.io.File;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

import javafx.scene.control.TreeItem;

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

    public static TreeItem<String> rootTreeView(ArrayList<Fight> fights) {
        TreeItem<String> root = new TreeItem<String>("Combat Fights");
        root.setExpanded(true);
        Iterator<Fight> iter = fights.iterator();
        int i = 0;
        while(iter.hasNext()) {
            Fight fight = iter.next();
            TreeItem<String> item = new TreeItem<String>(fight.info());
            if (i == 0) {
                item.setExpanded(true);
            }
            Iterator<Actor> iterActor = fight.getActors().iterator();
            while(iterActor.hasNext()) {
                Actor actor = iterActor.next();
                String dps = "";
                String hps = "";
                Date endTime = fight.getFinish();
                if (endTime == null) {
                    endTime = new Date();
                }
                long duration = endTime.getTime() - fight.getStart().getTime();
                if (duration > 100) {
                    dps = valuePerSecond(actor.getDamageDone(fight), duration);
                    hps = valuePerSecond(actor.getHealDone(fight), duration);
                    item.getChildren().add(new TreeItem<String>(actor.info(fight, dps, hps)));
                }
            }
            root.getChildren().add(item);
            i++;
        }
        return root;
    }

    public static String valuePerSecond(double value, long duration) {
        final DecimalFormat df = new DecimalFormat("#.00");
        double result = value/(duration/1000);
        if (result > 0.01) {
            return df.format(result);
        }

        return "0.00";
    }
}
