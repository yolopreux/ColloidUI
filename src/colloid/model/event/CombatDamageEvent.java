package colloid.model.event;

import java.util.Date;

import colloid.model.event.Combat.Ability;
import colloid.model.event.Combat.Actor;
import colloid.model.event.Combat.Effect;
import colloid.model.event.Combat.Target;

public class CombatDamageEvent implements Combat.Event {
    Date timestamp;
    Actor actor;
    Target target;
    Effect<Ability> effect;

    @Override
    public void add(Actor actor, Target target, Effect<Ability> effect) {
        timestamp = new Date();
        this.actor = actor;
        this.target = target;
        this.effect = effect;
    }

    @Override
    public void add(Actor actor, Effect<Ability> effect) {
        timestamp = new Date();
        this.actor = actor;
        this.target = null;
        this.effect = effect;
    }

}
