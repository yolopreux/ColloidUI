package colloid.model.event;

import colloid.model.event.RecountLoop.InterruptLoopException;

public interface Combat {

    public Combat add(Actor actor, Target target, Effect<Ability> effect);
    public Combat compile();

    public interface Recount {
        public void onStart();
        public void onUpdate();
        public void onStop();
        public void init();
    }

    public interface Entity {
        public void compile();
    }

    public interface Character extends Entity {
        public boolean isPlayer();
        public String name();
    }

    public interface Actor extends Character {
        public void setOnHeal(CombatHealEvent event);
        public void setOnDamage(CombatDamageEvent event);
        public void setOnCombatEnter(CombatEnterEvent event);
        public void setOnCombatExit(CombatExitEvent event);
    }

    public interface Target extends Character {
        public void setOnHeal(CombatHealEvent event);
        public void setOnDamage(CombatDamageEvent event);
        public void setOnCombatEnter(CombatEnterEvent event);
        public void setOnCombatExit(CombatExitEvent event);
    }

    public interface Ability extends Entity {
        public String name();
        public void setOnHeal(CombatHealEvent event);
        public void setOnDamage(CombatDamageEvent event);
        public void setOnCombatEnter(CombatEnterEvent event);
        public void setOnCombatExit(CombatExitEvent event);
    }

    public interface Effect<T> extends Entity {
        public void setOnHeal(CombatHealEvent event);
        public void setOnDamage(CombatDamageEvent event);
        public void setOnCombatEnter(CombatEnterEvent event);
        public void setOnCombatExit(CombatExitEvent event);

        public interface Heal {
        }

        public interface Hit {
        }
    }

    public interface Event {
        public void add(Actor actor, Target target, Effect<Ability> effect);
        public void add(Actor actor, Effect<Ability> effect);
    }
}
