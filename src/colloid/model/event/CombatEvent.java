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
import java.util.EventObject;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import colloid.model.event.Actor;
import colloid.model.event.DoesNotExist;
import colloid.model.event.Target;
import colloid.model.event.Combat;
import colloid.model.event.Fight;


public class CombatEvent extends EventObject implements Combat.Event {

    private static final long serialVersionUID = -2692281973951200556L;
    protected String logdata;
    static final Pattern pattern = Pattern.compile(".*\\((\\d+)(.*)");
    protected Combat.Actor actor;
    protected Combat.Target target;
    protected Combat.Effect effect;
    protected Fight fight;
    protected Date timestamp;
    protected Ability ability;
    protected double value;

    public enum EventType {
        HEAL, DAMAGE
    }


    public CombatEvent(Object source) {
        super(source);
        timestamp = new Date();
    }

    public CombatEvent(Object source, String logdata) {
        super(source);
        timestamp = new Date();
        this.logdata = logdata;
        try {
            actor = new Actor(logdata);
        } catch (DoesNotExist e1) {
            actor = null;
        }
        try {
            target = new Target(logdata);
        } catch (DoesNotExist e) {
            target = null;
        }
        try {
            ability = new Ability(this);
        } catch (DoesNotExist ex) {
            ability = null;
        }
    }

    public CombatEvent(Object source, String logdata, Fight fight) {
        super(source);
        timestamp = new Date();
        this.logdata = logdata;
        try {
            actor = new Actor(logdata);
        } catch (DoesNotExist e1) {
            actor = null;
        }
        try {
            target = new Target(logdata);
        } catch (DoesNotExist e) {
            target = null;
        }
        this.fight = fight;
        try {
            ability = new Ability(logdata);
        } catch (DoesNotExist ex) {
            ability = null;
        }
    }

    @Override
    public Fight getFight() {
        return this.fight;
    }

    @Override
    public void setFight(Fight fight) {
        this.fight = fight;
    }

    @Override
    public void add(Combat.Actor actor, Combat.Target target, Combat.Effect effect) {
        this.actor = actor;
        this.target = target;
        this.effect = effect;
    }

    @Override
    public void add(Combat.Actor actor, Combat.Effect effect) {
        this.actor = actor;
        this.target = null;
        this.effect = effect;
    }

    public String info() {
        return String.format("%s --> %s -> %s",
                actor.getName(), target.getName(), value);
    }

    @Override
    public String toString() {
        return String.format("CombatEvent [actor=%s, fight=%s, value=%s]",
                actor, fight, value);
    }

    public String getLogdata() {
        return logdata;
    }

    @Override
    public double getValue() {
        if (Double.isNaN(value) || value == 0) {
            value = parseValue(logdata);
        }
        return value;
    }

    public static double parseValue(String logdata) {
        double result = 0;
        String[] data = logdata.substring(1).split("\\]\\s\\[");
        try {
            Matcher matcher = pattern.matcher(data[4].trim());
            if (matcher.matches()) {
                String firstGroup = matcher.group(1);
                result = Double.parseDouble(firstGroup);
            }
        } catch (NumberFormatException ex) {
            System.out.println(ex);
        } catch (IllegalStateException ex) {
            System.out.println(ex);
        }

        return result;
    }

    void compile(String logdata) {
        try {
            try {
                actor = new Actor(logdata);
            } catch (DoesNotExist e1) {
                actor = null;
            }
            try {
                target = new Target(logdata);
            } catch (DoesNotExist e) {
                actor = null;
            }
            try {
                ability = new Ability(logdata);
            } catch (DoesNotExist ex) {
                ability = null;
            }
            //effect = data[4];
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println(e);
        }
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public Ability getAbility() {
        return ability;
    }

    public void setAbility(Ability ability) {
        this.ability = ability;
    }

    public Combat.Actor getActor() {
        return actor;
    }
}
