/**
 *  Colloid project
 *
 *  Combat log analyzer.
 *
 *  copyright: (c) 2013 by Darek <netmik12 [AT] gmail [DOT] com>
 *  license: BSD, see LICENSE for more details
 */
package colloid.model.event;

public class CombatHealEvent extends CombatEvent implements Combat.Event {


    private static final long serialVersionUID = -3960047681632022561L;

    public CombatHealEvent(Object source) {
        super(source);
    }

    public CombatHealEvent(Object source, String logdata) {
        super(source, logdata);
    }

    public static CombatHealEvent createEvent(String logdata) {
        return null;
    }
}
