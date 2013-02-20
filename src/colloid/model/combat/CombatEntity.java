package colloid.model.combat;

public abstract class CombatEntity {

    String logData;
    String name;
    String type;
    double threat;
    double value;

    public CombatEntity(String log) {
        logData = log;
    }
    abstract void parse();

    @Override
    public String toString() {
        return "CombatEntity [logData=" + logData + ", name=" + name
                + ", type=" + type + ", threat=" + threat + ", value="
                + value + "]";
    }
}
