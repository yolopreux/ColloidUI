package colloid.model.event;

import java.io.File;
import java.util.Date;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Platform;
import colloid.model.LogUtil;
import colloid.model.Recount;
import colloid.model.event.Combat.EventHandler;
import colloid.model.event.Combat.ObservableListString;


public abstract class RecountLoop implements Combat.Recount {

    private boolean isRunning;
    protected HashSet<Actor> actors= new HashSet<Actor>();
    String combatDirPath;
    EventHandler<CombatEvent> handler;
    protected ObservableListString observableObj;
    String lastLine;
    Date currentLogTime;

    public RecountLoop() {
    }

    @Override
    public void init() {
        run();
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onUpdate() {
    }

    @Override
    public void onStop() {
    }

    protected void update() {
        try {
            compileCurrent();
        } catch (InvalidCombatDirPathException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

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

    void restart() {
        stop();
        while (!isRunning) {
            run();
            break;
        }
    }

    public void run() {
        isRunning = true;
        new RecountThread(new Runnable() {
            void runLater() {
                if (Platform.isFxApplicationThread()) {
                    update();
                } else {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Platform.runLater(new Runnable() {
                        @Override public void run() {
                            runLater();
                        }
                    });
                }
            }
            @Override
            public void run() {
                onStart();
                while (isRunning) {
                    runLater();
                }
            }
        }).start();
    }

    public void stop() {
        isRunning = false;
        onStop();
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void interrupt() throws InterruptLoopException {
        throw new InterruptLoopException();
    }

    public class InterruptLoopException extends Exception {
        private static final long serialVersionUID = 2213013019370707528L;
    }

    public RecountLoop setCombatDirPath(String combatDirPath) {
        this.combatDirPath = combatDirPath;

        return this;
    }

    public void setObservable(Combat.ObservableListString observable) {
        observableObj = observable;
    }

    public String getCombatDirPath() {
        return combatDirPath;
    }

    @Override
    public void onUpdate(EventHandler<CombatEvent> handler) {
        this.handler = handler;
    }

    public class InvalidCombatDirPathException extends Exception {
        private static final long serialVersionUID = -346778478900727174L;
    }

    private void compileCurrent() throws InvalidCombatDirPathException {
        if (getCombatDirPath() == null) {
            throw new InvalidCombatDirPathException();
        }
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
                currentLogTime = Util.parseDate(line);
                if (currentLogTime == null) {
                    continue;
                }
                if (lastLine == null || !Util.isDone(currentLogTime, lastLine)) {
                    lastLine = line;
                    if (handler != null) {
                        handler.handle(new CombatEvent(this, lastLine));
                    }
                    onUpdate();
                }
            }
        }
    }

    public String getCurrentLogadata() {
       return lastLine;
    }

    public boolean isStateChanged() {
        return !Util.isDone(currentLogTime, lastLine);
    }

    public void registerActor(Actor actor) {
        actors.add(actor);
    }

    public HashSet<Actor> getActors() {
        return actors;
    }
}
