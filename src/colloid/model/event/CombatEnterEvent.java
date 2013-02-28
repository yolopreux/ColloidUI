package colloid.model.event;

public class CombatEnterEvent extends CombatEvent {

    private static final long serialVersionUID = 9172175510901559820L;

    public CombatEnterEvent(Object source) {
        super(source);
    }

    public CombatEnterEvent(Object source, String logdata) {
        super(source, logdata);
    }
}
