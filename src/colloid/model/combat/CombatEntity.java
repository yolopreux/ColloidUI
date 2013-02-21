package colloid.model.combat;

public abstract class CombatEntity implements ICombatEntity {

    String logData;
    String name;
    String type;
    double threat;
    double value;

    public CombatEntity(String log) {
        logData = log;
        parse();
    }

    @Override
    public String toString() {
        return "CombatEntity [logData=" + logData + ", name=" + name
                + ", type=" + type + ", threat=" + threat + ", value="
                + value + "]";
    }

    public String getLogData() {
        return logData;
    }

    public void fromString(String logData) {
        this.logData = logData;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getThreat() {
        return threat;
    }

    public void setThreat(double threat) {
        this.threat = threat;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
