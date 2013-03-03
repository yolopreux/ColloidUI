package colloid.model.event;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Platform;
import colloid.model.LogUtil;
import colloid.model.event.Character.DoesNotExist;
import colloid.model.event.Combat.Event;
import colloid.model.event.Combat.EventHandler;
import colloid.model.event.Combat.ObservableListString;


public abstract class RecountLoop implements Combat.Recount {

    private boolean isRunning;
    protected HashSet<Actor> actors= new HashSet<Actor>();
    protected HashSet<Fight> fights= new HashSet<Fight>();
    String combatDirPath;
    HashSet<EventHandler<CombatEvent>> handler = new HashSet<EventHandler<CombatEvent>>();
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
                Logger.getLogger(RecountLoop.class.getName()).log(Level.SEVERE, null, ex);
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
                        Thread.sleep(200);
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

    /**
     * @TODO stop thread should use InterruptLoopException
     * @throws InterruptLoopException
     */
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
        this.handler.add(handler);
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
            String tailLines;
            try {
                tailLines = LogUtil.tail(log, 30);
            } catch (StringIndexOutOfBoundsException ex) {
                Logger.getLogger(RecountLoop.class.getName()).log(Level.SEVERE, "Invalid log combat source");
                return;
            }
            String[] lines = tailLines.split("\n");
            for (String line : lines) {
                currentLogTime = Util.parseDate(line);
                if (currentLogTime == null) {
                    continue;
                }
                if (lastLine == null || !Util.isDone(currentLogTime, lastLine)) {
                    lastLine = line;
                    if (!handler.isEmpty()) {
                        beforeUpdate(lastLine);
                        Iterator<EventHandler<CombatEvent>> iter = handler.iterator();
                        while (iter.hasNext()) {
                            iter.next().handle(new CombatEvent(this, lastLine));
                        }
                    }
                    onUpdate();
                }
            }
        }
    }

    /**
     * Executes before combat event update
     */
    protected void beforeUpdate(String combatlog) {
        try {
            registerActor(new Actor(combatlog));
        } catch (DoesNotExist e) {
            //do nothing
        }
        Iterator<Actor> iterActor = getActors().iterator();
        while(iterActor.hasNext()) {
            Actor act = iterActor.next();
            act.handleEvent(combatlog);
        }
    }

    public String getCurrentLogadata() {
       return lastLine;
    }

    public boolean isStateChanged() {
        return !Util.isDone(currentLogTime, lastLine);
    }

    public void registerActor(Actor actor) {
        if (!actors.contains(actor)) {
            actor.setOnDamage(new Combat.EventHandler<Combat.Event>() {
                @Override
                public void handle(Event event) {
                }
            });
            actor.setOnHeal(new Combat.EventHandler<Combat.Event>() {
                @Override
                public void handle(Event event) {
                }
            });
            actor.setOnCombatEnter(new Combat.EventHandler<CombatEnterEvent>() {
                @Override
                public void handle(CombatEnterEvent event) {
                    // TODO Auto-generated method stub
                }
            });
            actor.setOnCombatExit(new Combat.EventHandler<CombatExitEvent>() {
                @Override
                public void handle(CombatExitEvent event) {
                }
            });
            actors.add(actor);
        }
    }

    public HashSet<Actor> getActors() {
        return actors;
    }


    /**
     * Return sorted list of actors by damage done
     */
    public ArrayList<Actor> getActorList() {
        ArrayList<Actor> list = new ArrayList<Actor>(getActors());
        Collections.sort(list, Collections.reverseOrder());

        return list;
    }

    public ArrayList<Fight> getFightList() {
        ArrayList<Fight> list = new ArrayList<Fight>(getFights());
        Collections.sort(list, Collections.reverseOrder());

        return list;
    }


    public HashSet<Fight> getFights() {
        return fights;
    }

    public void setFights(HashSet<Fight> fights) {
        this.fights = fights;
    }
}
