package colloid.model.event;

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
    Effect effect;

    @Override
    public void add(Actor actor, Target target, Effect effect) {
        this.actor = actor;
        this.target = target;
        this.effect = effect;
    }

    @Override
    public void add(Actor actor, Effect effect) {
        this.actor = actor;
        this.target = null;
        this.effect = effect;
    }

    public static CombatHealEvent createEvent(String logdata) {
        return null;
    }
}
