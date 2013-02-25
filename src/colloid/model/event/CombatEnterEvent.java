package colloid.model.event;

import colloid.model.event.Combat.Ability;
import colloid.model.event.Combat.Actor;
import colloid.model.event.Combat.Effect;
import colloid.model.event.Combat.Target;

public class CombatEnterEvent implements Combat.Event {

    @Override
    public void add(Actor actor, Target target, Effect<Ability> effect) {
        // TODO Auto-generated method stub

    }

    @Override
    public void add(Actor actor, Effect<Ability> effect) {
        // TODO Auto-generated method stub

    }

}
