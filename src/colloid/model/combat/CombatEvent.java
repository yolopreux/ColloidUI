package colloid.model.combat;

import colloid.model.combat.CombatEntity;
import colloid.model.combat.Effect;

public class CombatEvent {

    CombatEntity actor;
    CombatEntity target;
    CombatEntity ability;
    Effect effect;


    public CombatEvent(final String[] data) {
        try {
            actor = new CombatEntity(data[1]) {
                @Override
                public void parse() {
                    name = logData;
                }
            };
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println(e);
        }
        try {
            target = new CombatEntity(data[2]) {
                @Override
                public void parse() {
                    name = logData;
                }
            };
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println(e);
        }
        try {
            ability = new CombatEntity(data[3]) {
                @Override
                public void parse() {
                }
            };
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println(e);
        }
        try {
            effect = new Effect(data[4]) {
                @Override
                public void parse() {
                    parseValue();
                }

                @Override
                public boolean isEnterCombat() {
                    return logData.contains("EnterCombat");
                }

                @Override
                public boolean isExitCombat() {
                    return logData.contains("ExitCombat");
                }

                @Override
                public boolean isDamage() {
                    return logData.contains("Damage");
                }

                @Override
                public boolean isHeal() {
                    return logData.contains("Heal");
                }
            };
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println(e);
        }
    }

    @Override
    public String toString() {
        return "CombatEvent [actor=" + actor + ", target=" + target
                + ", ability=" + ability + ", effect=" + effect + "]";
    }
}
