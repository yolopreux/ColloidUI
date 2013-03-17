/**
 *  Colloid project
 *
 *  Combat log analyzer.
 *
 *  copyright: (c) 2013 by Darek <netmik12 [AT] gmail [DOT] com>
 *  license: BSD, see LICENSE for more details
 */
package colloid.model.event;

import java.util.Comparator;
import java.util.Iterator;

import colloid.model.event.Combat;
import colloid.model.event.Combat.Event;

public class Actor extends Character implements Combat.Actor, Comparable<Actor> {

    public Actor(String logdata) throws DoesNotExist {
        super(logdata);
    }

    @Override
    public boolean isPlayer() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void compile() throws DoesNotExist {
        try {
            String[] items = logdata.substring(1).split("\\]\\s\\[");
            if (items[1].contains("{")) {
                name = items[1].substring(0, items[1].indexOf("{")-1);
                return;
            }
            name = items[1];

        } catch (StringIndexOutOfBoundsException ex) {
            throw new DoesNotExist();
        } catch (ArrayIndexOutOfBoundsException ex) {
            throw new DoesNotExist();
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Actor other = (Actor) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }

    public double getDamageDone() {
        return getValueDone(new EventFilter() {
            @Override boolean hasEvent(Event event) {
                if (event instanceof CombatDamageEvent) {
                    return true;
                }
                return false;
            }
        });
    }

    public double getDamageDone(final Fight fight) {
        if (fight == null) {
            return 0;
        }
        return getValueDone(new EventFilter() {
            @Override boolean hasEvent(Event event) {
                if (event.getFight() == null) {
                    return false;
                }
                if (event instanceof CombatDamageEvent && event.getFight().equals(fight)) {
                    return true;
                }
                return false;
            }

            public boolean isOld(Event event) {
                if (fight.start.getTime() < ((CombatEvent) event).getTimestamp().getTime()) {
                    return false;
                }
                return true;
            }
        });
    }

    public double getHealDone() {
        return getValueDone(new EventFilter() {
            @Override boolean hasEvent(Event event) {
                if (event instanceof CombatHealEvent) {
                    return true;
                }
                return false;
            }
        });
    }

    public double getHealDone(final Fight fight) {
        if (fight == null) {
            return 0;
        }
        return getValueDone(new EventFilter() {
            @Override boolean hasEvent(Event event) {
                if (event.getFight() == null) {
                    return false;
                }
                if (event instanceof CombatHealEvent && event.getFight().equals(fight)) {
                    return true;
                }
                return false;
            }
        });
    }

    protected double getValueDone(EventFilter filter) {
        double sum = 0;
        Iterator<Combat.Event> iter = events.iterator();
        while (iter.hasNext()) {
            Combat.Event event = iter.next();
            if (filter.hasEvent(event)) {
                sum += event.getValue();
            }
        }

        return sum;
    }

    abstract class EventFilter {
        abstract boolean hasEvent(Combat.Event event);
    }

    @Override
    public int compareTo(Actor o) {
        if (getDamageDone() > o.getDamageDone()) {
            return 1;
        }
        return -1;
    }

    public String info(String dps, String hps) {

        return String.format(
                "%s, damage: %s: dps: %s, heal: %s, hps: %s",
                getName(), getDamageDone(), dps, getHealDone(), hps);
    }

    public String info(Fight fight, String dps, String hps) {
        return String.format(
                "%s, damage: %s: dps: %s, heal: %s, hps: %s",
                getName(), getDamageDone(fight), dps, getHealDone(fight), hps);
    }


    @Override
    public String toString() {
        return String.format(
                "Actor [%s, DamageDone: %s, HealDone: %s]",
                getName(), getDamageDone(), getHealDone());
    }

    public class AbilityDamageDoneComparator implements Comparator<Ability> {
        @Override public int compare(Ability ability1, Ability ability2) {
            if (ability1.getValueDone() < ability2.getValueDone()) {
                return 1;
            }
            return -1;
        }
    }
}
