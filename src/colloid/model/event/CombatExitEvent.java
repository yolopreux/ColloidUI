/**
 *  Colloid project
 *
 *  Combat log analyzer.
 *
 *  copyright: (c) 2013 by Darek <netmik12 [AT] gmail [DOT] com>
 *  license: BSD, see LICENSE for more details
 */
package colloid.model.event;

public class CombatExitEvent  extends CombatEvent implements Combat.Event {

    private static final long serialVersionUID = 9213411041279869375L;

    public CombatExitEvent(Object source) {
        super(source);
    }

    public CombatExitEvent(Object source, String logdata) {
        super(source, logdata);
    }

}
