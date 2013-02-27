package colloid.model.event;

import java.util.EventObject;

import colloid.model.event.Combat.Ability;
import colloid.model.event.Combat.Actor;
import colloid.model.event.Combat.Effect;
import colloid.model.event.Combat.Event;
import colloid.model.event.Combat.Target;

public class CombatEvent extends EventObject implements Combat.Event {

    private static final long serialVersionUID = -2692281973951200556L;

    public CombatEvent(Object source) {
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

    public static CombatEvent createEvent(String logdata) {
        return null;
    }

}
