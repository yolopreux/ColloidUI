package colloid.model.event;

import colloid.model.event.Combat.Ability;
import colloid.model.event.Combat.Actor;
import colloid.model.event.Combat.Effect;
import colloid.model.event.Combat.Target;

public class CombatHealEvent extends CombatEvent implements Combat.Event {

    private static final long serialVersionUID = -3960047681632022561L;

    public CombatHealEvent(Object source) {

        super(source);
    }

    Actor actor;
    Target target;
    Effect<Ability> effect;

    @Override
    public void add(Actor actor, Target target, Effect<Ability> effect) {
        this.actor = actor;
        this.target = target;
        this.effect = effect;
    }

    @Override
    public void add(Actor actor, Effect<Ability> effect) {
        this.actor = actor;
        this.target = null;
        this.effect = effect;
    }
}
