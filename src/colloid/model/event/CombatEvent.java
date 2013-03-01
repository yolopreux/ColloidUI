package colloid.model.event;

import java.util.EventObject;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import colloid.model.event.Actor;
import colloid.model.event.Target;
import colloid.model.event.Effect;
import colloid.model.event.Combat;

import colloid.model.combat.CombatEntity;

public class CombatEvent extends EventObject implements Combat.Event {

    private static final long serialVersionUID = -2692281973951200556L;
    protected String logdata;
    static final Pattern pattern = Pattern.compile(".*\\((\\d+)(.*)");
    Combat.Actor actor;
    Combat.Target target;
    Combat.Effect effect;

    protected double value;

    public CombatEvent(Object source) {
        super(source);
    }

    public CombatEvent(Object source, String logdata) {
        super(source);
        this.logdata = logdata;
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

    @Override
    public String toString() {
        return "CombatEvent [logdata=" + logdata + ", actor=" + actor
                + ", target=" + target + ", effect=" + effect + "]";
    }

    public String getLogdata() {
        return logdata;
    }

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
        String[] data = logdata.substring(1).split("\\]\\s\\[");
        try {
            actor = new Actor(logdata);
            target = new Target(logdata);
//            ability = data[3]
//            effect = data[4];
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println(e);
        }
    }


}
