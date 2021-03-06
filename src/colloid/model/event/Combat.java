/**
 *  Colloid project
 *
 *  Combat log analyzer.
 *
 *  copyright: (c) 2013 by Darek <netmik12 [AT] gmail [DOT] com>
 *  license: BSD, see LICENSE for more details
 */
package colloid.model.event;

import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.collections.ObservableSet;
import colloid.model.event.DoesNotExist;
import colloid.model.event.RecountLoop.InterruptLoopException;

public interface Combat {

    public Combat add(Actor actor, Target target, Effect effect);
    public Combat compile();

    public interface Recount {
        public void onStart();
        public void onUpdate();
        public void onStop();
        public void init();
        public void onUpdate(EventHandler<CombatEvent> handler);
    }

    public interface RegisterHandler {
        public void setOnHeal(EventHandler<Combat.Event> handler);
        public void setOnDamage(EventHandler<Combat.Event> handler);
        public void setOnCombatEnter(EventHandler<CombatEnterEvent> handler);
        public void setOnCombatExit(EventHandler<CombatExitEvent> handler);
    }

    public interface ObservableListString {
        public ObservableList<String> getList();
        public ObservableSet<String> getSet();
        public ObservableMap<String, String> getMap();
    }

    public interface Entity {
        public void compile() throws DoesNotExist;
    }

    public interface Character extends Entity {
        public boolean isPlayer();
        public String getName();
        public void setOnHeal(EventHandler<Combat.Event> handler);
        public void setOnDamage(EventHandler<Combat.Event> handler);
        public void setOnCombatEnter(EventHandler<CombatEnterEvent> handler);
        public void setOnCombatExit(EventHandler<CombatExitEvent> handler);
    }

    public interface Actor extends Character {
    }

    public interface Target extends Character {
    }

    public interface Ability extends Entity {
        public String name();
        public void setOnHeal(EventHandler<CombatHealEvent> handler);
        public void setOnDamage(EventHandler<CombatDamageEvent> handler);
        public void setOnCombatEnter(EventHandler<CombatEnterEvent> handler);
        public void setOnCombatExit(EventHandler<CombatExitEvent> handler);
    }

    public interface Effect extends Entity {
        public void setOnHeal(EventHandler<CombatHealEvent> handler);
        public void setOnDamage(EventHandler<CombatDamageEvent> handler);
        public void setOnCombatEnter(EventHandler<CombatEnterEvent> handler);
        public void setOnCombatExit(EventHandler<CombatExitEvent> handler);

        public interface Heal {
        }

        public interface Hit {
        }
    }

    public interface Event {
        public void add(Actor actor, Target target, Effect effect);
        public void add(Actor actor, Effect effect);
        public double getValue();
        public void setFight(Fight current);
        public Fight getFight();
    }

    public interface EventHandler<E> {
        public void handle(E event);
    }
}
