package colloid.model.event;

public class CombatExitEvent  extends CombatEvent implements Combat.Event {

    private static final long serialVersionUID = 9213411041279869375L;

    public CombatExitEvent(Object source) {
        super(source);
    }

}
