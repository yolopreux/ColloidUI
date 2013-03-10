package colloid.model.control;

import java.util.Date;
import colloid.model.event.Actor;
import colloid.model.event.Fight;
import colloid.model.event.Util;

public class DamageTreeItem  implements Comparable<DamageTreeItem> {

    protected Actor actor;
    protected Fight fight;
    protected String dps;
    protected double damageDone;

    public DamageTreeItem(Actor actor, Fight fight) {
        init(actor, fight);
    }

    protected void init(Actor actor, Fight fight) {
        Date endTime = fight.getFinish();
        if (endTime == null) {
            endTime = new Date();
        }
        long duration = endTime.getTime() - fight.getStart().getTime();
        damageDone = actor.getDamageDone(fight);
        if (duration > 100) {
            dps = Util.valuePerSecond(damageDone, duration);
        }
    }

    public String info() {
        return String.format("%s: %s(%s)", actor.getName(), damageDone, dps);
    }

    @Override
    public int compareTo(DamageTreeItem o) {
        if (getDamageDone() > o.getDamageDone()) {
            return 1;
        }
        return -1;
    }

    @Override
    public String toString() {
        return info();
    }

    public double getDamageDone() {
        return damageDone;
    }

    public void setDamageDone(double damageDone) {
        this.damageDone = damageDone;
    }
}
