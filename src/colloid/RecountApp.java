package colloid;

import java.io.File;
import java.util.Date;

import javafx.collections.ObservableList;
import colloid.model.IRecount;
import colloid.model.LogUtil;
import colloid.model.Recount;
import colloid.model.event.RecountLoop;
import colloid.model.event.Util;

public class RecountApp  extends RecountLoop {

    ObservableList<String> textLog;
    private static RecountApp instance;
    String lastLine;

    protected RecountApp() {
    }

    public static RecountApp getInstance() {
        if (instance == null) {
            instance = new RecountApp();
        }

        return instance;
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onUpdate() {
        updateLog();
    }

    @Override
    public void onStop() {
    }

    public void setTextLog(final ObservableList<String> textLog) {
        this.textLog = textLog;
    }

    private void updateLog() {
        File[] logs = Util.filesByPath(getCombatDirPath());
        if (logs == null || logs.length == 0) {
            return;
        }
        File log = logs[logs.length - 1];
        /**
         * @TODO
         */
        if (log.isFile()) {
            String tailLines = LogUtil.tail(log, 30);
            String[] lines = tailLines.split("\n");
            for (String line : lines) {
                Date logTime = Util.parseDate(line);
                if (logTime == null) {
                    continue;
                }
                if (lastLine == null || !Util.isDone(logTime, lastLine)) {
                    lastLine = line;
                    if (observableObj != null && line != null) {
                        observableObj.getList().add(0, line);
                    }
                }
            }
        }
    }
}
