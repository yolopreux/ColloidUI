/**
 *  Colloid project
 *
 *  Combat log analyzer.
 *
 *  copyright: (c) 2013 by Darek <netmik12 [AT] gmail [DOT] com>
 *  license: BSD, see LICENSE for more details
 */
package colloid.model.event;

import java.util.Date;

import colloid.model.event.Combat.Actor;
import colloid.model.event.Combat.Effect;
import colloid.model.event.Combat.Target;

public class CombatDamageEvent extends CombatEvent {

    private static final long serialVersionUID = 827463100092857897L;

    Date timestamp;
    Actor actor;
    Target target;
    Effect effect;

    public CombatDamageEvent(Object source) {
        super(source);
    }

    public CombatDamageEvent(Object source, String logdata) {
        super(source, logdata);
    }

    @Override
    public void add(Actor actor, Target target, Effect effect) {
        timestamp = new Date();
        this.actor = actor;
        this.target = target;
        this.effect = effect;
    }

    @Override
    public void add(Actor actor, Effect effect) {
        timestamp = new Date();
        this.actor = actor;
        this.target = null;
        this.effect = effect;
    }

}
