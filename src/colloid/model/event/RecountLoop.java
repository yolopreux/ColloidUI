package colloid.model.event;

import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Platform;
import colloid.model.Recount;
import colloid.model.event.Combat.ObservableListString;


public abstract class RecountLoop implements Combat.Recount {

    private boolean isRunning;
    protected HashSet<Combat.Actor> actors= new HashSet<Combat.Actor>();
    String combatDirPath;

    protected ObservableListString observableObj;

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
        actors.add(new Actor(""));
        actors.iterator();
    }

    @Override
    public void onStop() {
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
                    onUpdate();
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

    public void setCombatDirPath(String combatDirPath) {
        this.combatDirPath = combatDirPath;
    }

    public void setObservable(Combat.ObservableListString observable) {
        observableObj = observable;
    }

    public String getCombatDirPath() {
        return combatDirPath;
    }

}
