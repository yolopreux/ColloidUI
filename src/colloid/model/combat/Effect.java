package colloid.model.combat;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import colloid.model.combat.CombatEntity;

public abstract class Effect extends CombatEntity {

    public Effect(String log) {
        super(log);
    }

    public abstract boolean isEnterCombat();
    public abstract boolean isExitCombat();
    public abstract boolean isDamage();
    public abstract boolean isHeal();

    public double getValue() {
        parseValue();

        return this.value;
    }

    public void parseValue() {
        double result = 0;

        Matcher matcher = Pattern.compile("\\(([0-9]{0,})(.*)\\)").matcher(logData);
        try {
            result = Double.parseDouble(matcher.group(1));
        } catch (NumberFormatException ex) {
            System.out.println(ex);
        } catch (IllegalStateException ex) {
            System.out.println(ex);
        }

        this.value = result;
    }

}
