package colloid.model.event;

import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Platform;
import javafx.concurrent.Task;

import colloid.model.Recount;
import colloid.model.IRecount.UpdateRecountLog;
import colloid.model.IRecount.UpdateTextLog;


public abstract class RecountLoop implements Combat.Recount {

    private boolean isRunning;

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
//        Task<String> task = new Task<String>() {
//            public void update() {
//                if (Platform.isFxApplicationThread()) {
//                    onUpdate();
//                } else {
//                    Platform.runLater(new Runnable() {
//                        @Override public void run() {
//                            onUpdate();
//                        }
//                    });
//                }
//            }
//
//            @Override
//            public String call() {
//                onStart();
//                while (isRunning) {
//                    update();
//                }
//                return null;
//            }
//        };
//
////        recountLog.textProperty().bind(task.messageProperty());
//        new RecountThread(task).start();
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
}
