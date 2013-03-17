/**
 *  Colloid project
 *
 *  Combat log analyzer.
 *
 *  copyright: (c) 2013 by Darek <netmik12 [AT] gmail [DOT] com>
 *  license: BSD, see LICENSE for more details
 */
package colloid.model.event;

public class CombatDamageEvent extends CombatEvent {


    private static final long serialVersionUID = 827463100092857897L;

    public CombatDamageEvent(Object source) {
        super(source);
    }

    public CombatDamageEvent(Object source, String logdata) {
        super(source, logdata);
    }

}
