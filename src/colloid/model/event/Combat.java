package colloid.model.event;

import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.collections.ObservableSet;
import colloid.model.event.RecountLoop.InterruptLoopException;

public interface Combat {

    public Combat add(Actor actor, Target target, Effect effect);
    public Combat compile();

    public interface Recount {
        public void onStart();
        public void onUpdate();
        public void onStop();
        public void init();
    }

    public interface ObservableListString {
        public ObservableList<String> getList();
        public ObservableSet<String> getSet();
        public ObservableMap<String, String> getMap();
    }

    public interface Entity {
        public void compile();
    }

    public interface Character extends Entity {
        public boolean isPlayer();
        public String getName();
    }

    public interface Actor extends Character {
        public void setOnHeal(EventHandler<CombatHealEvent> handler);
        public void setOnDamage(EventHandler<CombatDamageEvent> handler);
        public void setOnCombatEnter(EventHandler<CombatEnterEvent> handler);
        public void setOnCombatExit(EventHandler<CombatExitEvent> handler);
    }

    public interface Target extends Character {
        public void setOnHeal(EventHandler<CombatHealEvent> handler);
        public void setOnDamage(EventHandler<CombatDamageEvent> handler);
        public void setOnCombatEnter(EventHandler<CombatEnterEvent> handler);
        public void setOnCombatExit(EventHandler<CombatExitEvent> handler);
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
    }

    public interface EventHandler<E> {
        public void handle(E event);
    }
}
