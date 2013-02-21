package colloid.model.combat;

public abstract class CombatEntity implements ICombatEntity {

    String logData;
    String name;
    String type;
    double threat;
    double value;

    public CombatEntity(String log) {
        logData = log;
    }

    @Override
    public String toString() {
        return "CombatEntity [logData=" + logData + ", name=" + name
                + ", type=" + type + ", threat=" + threat + ", value="
                + value + "]";
    }
}
