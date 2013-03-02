package colloid.model.event;

import java.util.Iterator;

import colloid.model.event.Combat;

public class Actor extends Character implements Combat.Actor, Comparable<Actor> {

    public Actor(String logdata) {
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
    public void compile() {
        try {
            String[] items = logdata.substring(1).split("\\]\\s\\[");
            if (items[1].contains("{")) {
                name = items[1].substring(0, items[1].indexOf("{")-1);
                return;
            }
            name = items[1];

        } catch (StringIndexOutOfBoundsException ex) {
            ex.printStackTrace();
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
        double sum =0;
        Iterator<Combat.Event> iter = events.iterator();
        while (iter.hasNext()) {
            Combat.Event event = iter.next();
            if (event instanceof CombatDamageEvent) {
                sum += event.getValue();
            }
        }

        return sum;
    }

    public double getHealDone() {
        double sum =0;
        Iterator<Combat.Event> iter = events.iterator();
        while (iter.hasNext()) {
            Combat.Event event = iter.next();
            if (event instanceof CombatHealEvent) {
                sum += event.getValue();
            }
        }

        return sum;
    }

    @Override
    public int compareTo(Actor o) {
        if (getDamageDone() > o.getDamageDone()) {
            return 1;
        }
        return -1;
    }

    @Override
    public String toString() {
        return String.format(
                "Actor [%s, DamageDone: %s, HealDone: %s]",
                getName(), getDamageDone(), getHealDone());
    }

}
