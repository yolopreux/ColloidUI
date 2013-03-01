package colloid.model.event;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import colloid.RecountApp;
import colloid.model.event.Combat.Event;
import colloid.model.event.Combat.EventHandler;

public abstract class Character implements Combat.Character {

    protected String name;
    protected String logdata;
    protected EventHandler<Combat.Event> handlerDamage;
    protected EventHandler<Combat.Event> handlerHeal;
    protected ArrayList<Combat.Event> events = new ArrayList<Combat.Event>();

    public Character(String logdata) {

        this.logdata = logdata;
        compile();
    }

    @Override
    public boolean isPlayer() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void setOnHeal(EventHandler<Combat.Event> handler) {
        String logdata = RecountApp.getInstance().getCurrentLogadata();
        handlerHeal = handler;
        if (logdata != null && logdata.contains("Heal")) {
            Combat.Event event = new CombatHealEvent(this, logdata);
            handler.handle(event);
        }
    }

    @Override
    public void setOnDamage(EventHandler<Combat.Event> handler) {
        String logdata = RecountApp.getInstance().getCurrentLogadata();
        handlerDamage = handler;
        if (logdata != null && logdata.contains("Damage")) {
            CombatDamageEvent event = new CombatDamageEvent(this, logdata);
            handler.handle(event);
        }
    }

    @Override
    public void setOnCombatEnter(EventHandler<CombatEnterEvent> handler) {
        String logdata = RecountApp.getInstance().getCurrentLogadata();
        if (logdata != null && logdata.contains("CombatEnter")) {
            CombatEnterEvent event = new CombatEnterEvent(this, logdata);
            handler.handle(event);
        }
    }

    @Override
    public void setOnCombatExit(EventHandler<CombatExitEvent> handler) {
        String logdata = RecountApp.getInstance().getCurrentLogadata();
        if (logdata != null && logdata.contains("CombatExit")) {
            CombatExitEvent event = new CombatExitEvent(this, logdata);
            handler.handle(event);
        }
    }


    public void handleEvent(String logdata) {
        if (!equals(new Actor(logdata))) {
            return;
        }
        if (logdata != null && logdata.contains("Heal")) {
            if (handlerHeal != null) {
                Combat.Event healEvent = new CombatHealEvent(this, logdata);
                handlerHeal.handle(healEvent);
                events.add(healEvent);
            }
        }
        if (logdata != null && logdata.contains("Damage")) {
            if (handlerDamage != null) {
                CombatDamageEvent damageEvent = new CombatDamageEvent(this, logdata);
                handlerDamage.handle(damageEvent);
                events.add(damageEvent);
            }
//
        }
//            if (logdata != null && logdata.contains("CombatEnter")) {
//                CombatEnterEvent event = new CombatEnterEvent(this, logdata);
//                handler.handle(event);
//                events.add(event);
//            }
//            if (logdata != null && logdata.contains("CombatExit")) {
//                CombatExitEvent event2 = new CombatExitEvent(this, logdata);
//                handler.handle(event2);
//                events.add(event2);
//            }

    }

    public ArrayList<Combat.Event> getEvents() {
        return events;
    }

}
