package colloid.model;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.control.TextArea;
import colloid.model.combat.Combat;
import colloid.model.combat.ICombat;
@Deprecated
public class Recount implements IRecount {

    private final static int DEFAULT_LINE_TAIL_SIZE = 10;
    private static Recount instance;
    private boolean isRunning;
    protected File[] logs;
    String lastLine = null;
    ArrayList<String> data = new ArrayList<String>();
    ObservableList<String> textLog;
    TextArea recountLog;
    ICombat combat;
    boolean done = false;
    ObservableList<String> combatItems;
    protected int tailSize = DEFAULT_LINE_TAIL_SIZE;

    protected Recount() {
        combat = new Combat(this);
    }

    public TextArea getRecountLog() {
        return recountLog;
    }

    public static Recount getInstance() {
        if (instance == null) {
            instance = new Recount();
        }

        return instance;
    }

    @Deprecated
    class RecountThread extends Thread {
        public RecountThread(Runnable runnable) {
            super(runnable);
        }

        @Override
        public void run() {
            boolean done = false;
            try {
                super.run();
                done = true;
            } catch (Exception ex) {
                interrupt();
                Logger.getLogger(Recount.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                if (!done) {
                    interrupt();
                    restart();
                }
            }
        }
    }

    public void run() {
        isRunning = true;

        Task<String> task = new Task<String>() {
            public void addFight(final String item) {
                if (Platform.isFxApplicationThread()) {
                    combatItems.add(0, item);
                } else {
                    Platform.runLater(new Runnable() {
                        @Override public void run() {
                            addFight(item);
                        }
                    });
                }
            }

            public void addTextLog(final String text) {
                if (Platform.isFxApplicationThread()) {
                    textLog.add(0, text);
                } else {
                    Platform.runLater(new Runnable() {
                        @Override public void run() {
                            addTextLog(text);
                        }
                    });
                }
            }

            @Override
            public String call() {
                while (isRunning) {
                    parse(new UpdateTextLog() {
                        @Override
                        public void update(String text) {
                            addTextLog(text);
                        }
                        @Override
                        public void insert(String text) {
                            addTextLog(text);
                        }
                    },
                    /**
                     * @TODO move into service class
                     */
                    new UpdateRecountLog() {
                        @Override
                        public void update(String text) {
                            updateMessage(text);
                        }
                    });
                }

                return null;
            }
        };

        recountLog.textProperty().bind(task.messageProperty());
        new RecountThread(task).start();
    }

    public void stop() {
        isRunning = false;
    }

    public void restart() {
        stop();
        while (!isRunning) {
            run();
            break;
        }
    }

    public boolean isRunning() {
        return isRunning;
    }

    public Recount setCombatDirPath(String dirPath) {
        init(dirPath);
        return this;
    }

    private void init(String path) {
        File dir = new File(path);
        logs = dir.listFiles();
    }

    public void parse(IRecount.UpdateTextLog updatetextLog, IRecount.UpdateRecountLog updateRecountLog) {
        if (logs == null || logs.length == 0) {
            return;
        }
        File log = logs[logs.length - 1];
        /**
         * @TODO
         */
        if (log.isFile()) {
            String tailLines = LogUtil.tail(log, tailSize);
            String[] lines = tailLines.split("\n");
            for (String line : lines) {
                Date logTime = parseDate(line);
                if (logTime == null) {
                    continue;
                }
                if (lastLine == null || !isLogged(logTime)) {
                    lastLine = line;
                    if (textLog != null && line != null) {
                        updatetextLog.insert(line);
                        countCombat(line, updateRecountLog);
                    }
                }
            }
            tailSize = DEFAULT_LINE_TAIL_SIZE;
        }
    }

    private Date parseDate(String line) {
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

    private boolean isLogged(Date logtime) {
        if (logtime.getTime() > parseDate(lastLine).getTime()) {
            return false;
        }

        return true;
    }

    public ArrayList<String> getData() {
        return data;
    }

    public String getLastLine() {
        return lastLine;
    }

    public void setTextLog(final ObservableList<String> textLog) {
        this.textLog = textLog;
    }

    public ObservableList<String> getTextLog() {
        return textLog;
    }

    public void setRecountLog(final TextArea recountLog) {
        this.recountLog = recountLog;
    }

    public void countCombat(String event, IRecount.UpdateRecountLog updateRecountLog) {
        combat.add(event).recount(updateRecountLog);
    }

    public ICombat getCombat() {
        return combat;
    }

    public void setCombat(ICombat combat) {
        this.combat = combat;
    }

    public ObservableList<String> getCombatItems() {
        return combatItems;
    }

    public void setCombatItems(ObservableList<String> combatItems) {
        this.combatItems = combatItems;
    }

    public int getLineTail() {
        return tailSize;
    }

    public Recount withTail(int tail) {
        this.tailSize = tail;

        return this;
    }
}
