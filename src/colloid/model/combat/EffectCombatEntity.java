package colloid.model.combat;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import colloid.model.combat.CombatEntity;

public abstract class EffectCombatEntity extends CombatEntity {

    public EffectCombatEntity(String log) {
        super(log);
    }
    abstract boolean isEnterCombat();
    abstract boolean isExitCombat();
    abstract boolean isDamage();
    abstract boolean isHeal();

    double getValue() {
        parseValue();

        return this.value;
    }

    void parseValue() {
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
