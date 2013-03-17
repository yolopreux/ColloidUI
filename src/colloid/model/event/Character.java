/**
 *  Colloid project
 *
 *  Combat log analyzer.
 *
 *  copyright: (c) 2013 by Darek <netmik12 [AT] gmail [DOT] com>
 *  license: BSD, see LICENSE for more details
 */
package colloid.model.event;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import colloid.RecountApp;
import colloid.model.event.Combat.EventHandler;

public abstract class Character implements Combat.Character {

    protected String name;
    protected String logdata;
    protected HashSet<EventHandler<Combat.Event>> handlerDamage = new HashSet<EventHandler<Combat.Event>>();
    protected HashSet<EventHandler<Combat.Event>> handlerHeal = new HashSet<EventHandler<Combat.Event>>();
    protected ArrayList<Combat.Event> events = new ArrayList<Combat.Event>();
    protected HashSet<Combat.Ability> abilities = new HashSet<Combat.Ability>();

    public Character(String logdata) throws DoesNotExist {

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
        handlerHeal.add(handler);
        if (logdata != null && logdata.contains("Heal")) {
            Combat.Event event = new CombatHealEvent(this, logdata);
            handler.handle(event);
        }
    }

    @Override
    public void setOnDamage(EventHandler<Combat.Event> handler) {
        String logdata = RecountApp.getInstance().getCurrentLogadata();
        handlerDamage.add(handler);
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
        try {
            if (!equals(new Actor(logdata))) {
                return;
            }
        } catch (DoesNotExist e) {
            return;
        }
        if (logdata != null && logdata.contains("EnterCombat")) {
            CombatEnterEvent event = new CombatEnterEvent(this, logdata);
//            handler.handle(event);
            Fight.create();
            events.add(event);
        }

        if (logdata != null && logdata.contains("Heal")) {
            if (handlerHeal != null) {
                CombatHealEvent healEvent = new CombatHealEvent(this, logdata);
                if (Fight.inFight()) {
                    healEvent.setFight(Fight.current());
                    abilities.add(healEvent.getAbility());
                }
                Iterator<EventHandler<Combat.Event>> iter = handlerHeal.iterator();
                while(iter.hasNext()) {
                    iter.next().handle(healEvent);
                }
                events.add(healEvent);
            }
        }
        if (logdata != null && logdata.contains("Damage")) {
            if (handlerDamage != null) {
                CombatDamageEvent damageEvent = new CombatDamageEvent(this, logdata);
                if (Fight.inFight()) {
                    damageEvent.setFight(Fight.current());
                    abilities.add(damageEvent.getAbility());
                }
                Iterator<EventHandler<Combat.Event>> iter = handlerDamage.iterator();
                while(iter.hasNext()) {
                    iter.next().handle(damageEvent);
                }
                events.add(damageEvent);
            }
        }
        if (logdata != null && logdata.contains("ExitCombat")) {
            CombatExitEvent event2 = new CombatExitEvent(this, logdata);
            Fight.finish();
            events.add(event2);
        }
        if (Fight.inFight()) {
//            try {
//                abilities.add(new Ability(combatEvent));
//            } catch (DoesNotExist e) {
//                //e.printStackTrace();
//                //do nothing
//            }
            Fight.addActor((Actor) this);
            RecountApp.getInstance().getFights().add(Fight.current());
        }

    }

    public ArrayList<Combat.Event> getEvents() {
        return events;
    }

    public HashSet<Combat.Ability> getAbilities() {
        return abilities;
    }

    public void setAbilities(HashSet<Combat.Ability> abilities) {
        this.abilities = abilities;
    }
}
