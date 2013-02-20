package colloid.model.combat;

import colloid.model.combat.CombatEntity;
import colloid.model.combat.EffectCombatEntity;

public class CombatEvent {

    CombatEntity actor;
    CombatEntity target;
    CombatEntity ability;
    EffectCombatEntity effect;


    public CombatEvent(final String[] data) {
        try {
            actor = new CombatEntity(data[1]) {
                @Override
                void parse() {
                    name = logData;
                }
            };
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println(e);
        }
        try {
            target = new CombatEntity(data[2]) {
                @Override
                void parse() {
                    name = logData;
                }
            };
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println(e);
        }
        try {
            ability = new CombatEntity(data[3]) {
                @Override
                void parse() {
                }
            };
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println(e);
        }
        try {
            effect = new EffectCombatEntity(data[4]) {
                @Override
                void parse() {
                    parseValue();
                }

                @Override
                boolean isEnterCombat() {
                    return logData.contains("EnterCombat");
                }

                @Override
                boolean isExitCombat() {
                    return logData.contains("ExitCombat");
                }

                @Override
                boolean isDamage() {
                    return logData.contains("Damage");
                }

                @Override
                boolean isHeal() {
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
