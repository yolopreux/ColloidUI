package colloid;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import javafx.collections.ObservableList;
import colloid.model.IRecount;
import colloid.model.LogUtil;
import colloid.model.Recount;
import colloid.model.event.Actor;
import colloid.model.event.Combat;
import colloid.model.event.CombatDamageEvent;
import colloid.model.event.CombatHealEvent;
import colloid.model.event.RecountLoop;
import colloid.model.event.Util;

public class RecountApp  extends RecountLoop {

    ObservableList<String> textLog;
    private static RecountApp instance;

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

    /**
     * Executes in Fx application thread
     */
    @Override public void onUpdate() {
    }

    @Override
    public void onStop() {
    }

    public void setTextLog(final ObservableList<String> textLog) {
        this.textLog = textLog;
    }

    /**
     * Return sorted list of actors by damage done
     */
    public ArrayList<Actor> getActorList() {
        ArrayList<Actor> list = new ArrayList<Actor>(getActors());
        Collections.sort(list, Collections.reverseOrder());

        return list;
    }

}
