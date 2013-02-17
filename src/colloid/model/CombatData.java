package colloid.model;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

public class CombatData {
    public SimpleLongProperty id = new SimpleLongProperty();
    public SimpleStringProperty abilityName;
    public SimpleDoubleProperty abilityValue;

    public CombatData(Long id, String ability, double value) {
        this.id.set(id);
        abilityName = new SimpleStringProperty(ability);
        abilityValue = new SimpleDoubleProperty(value);
    }

    public CombatData(int id, String ability, double value) {
        this.id.set((long) id);
        abilityName = new SimpleStringProperty(ability);
        abilityValue = new SimpleDoubleProperty(value);
    }

    public Long getId() {
        return id.get();
    }

    public String getAbility() {
        return abilityName.get();
    }

    public double getValue() {
        return abilityValue.get();
    }

    public void setAbilityValue(double value) {
        abilityValue.set(value);
    }
}
