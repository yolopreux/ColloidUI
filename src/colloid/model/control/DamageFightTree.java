package colloid.model.control;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;

import colloid.model.event.Ability;
import colloid.model.event.Actor;
import colloid.model.event.Fight;
import colloid.model.event.Actor.AbilityHealDoneComparator;

public class DamageFightTree extends FightTree {

    public DamageFightTree(Fight fight) {
        super(fight);
        init(fight.new ActorDamageDoneComparator());
    }

    @Override protected double valueDone(Actor actor, Fight fight) {
        return actor.getDamageDone(fight);
    }

    @Override
    protected ArrayList<Ability> getAbilities(Actor actor) {
        ArrayList<Ability> items = new ArrayList<Ability>();

        Iterator<Ability> iterAbility = actor.getAbilities().iterator();
        while (iterAbility.hasNext()) {
            Ability ability = iterAbility.next();
            if (ability.getDamageDone() > 0) {
                items.add(ability);
            }
        }
        return items;
    }

    @Override
    protected String abilityInfo(Ability ability, double totalVal) {
        return ability.info(totalVal);
    }

    @Override
    protected Comparator<Ability> getAbilityComparator(Actor actor) {
        return actor.new AbilityValueDoneComparator();
    }
}
