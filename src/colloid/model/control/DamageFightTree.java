package colloid.model.control;


import colloid.model.event.Actor;
import colloid.model.event.Fight;

public class DamageFightTree extends FightTree {

    public DamageFightTree(Fight fight) {
        super(fight);
        init(fight.new ActorDamageDoneComparator());
    }

    @Override protected double valueDone(Actor actor, Fight fight) {
        return actor.getDamageDone(fight);
    }
}
