/**
 *  Colloid project
 *
 *  Combat log analyzer.
 *
 *  copyright: (c) 2013 by Darek <netmik12 [AT] gmail [DOT] com>
 *  license: BSD, see LICENSE for more details
 */
package colloid.model.event;

import colloid.model.event.DoesNotExist;
import colloid.model.event.Combat.EventHandler;

public class Ability implements Combat.Ability {

    protected String logdata;
    protected String name;
    protected double valueDone;

    public Ability(String logdata) throws DoesNotExist {
        this.logdata = logdata;

        compile();
    }

    public Ability(CombatEvent event) throws DoesNotExist {
        this.logdata = event.getLogdata();

        compile();
    }

    @Override
    public void compile() throws DoesNotExist {
        try {
            String[] items = logdata.substring(1).split("\\]\\s\\[");
            if (items[3] == null) {
                throw new DoesNotExist();
            }
            if (items[3].contains("{")) {
                name = items[3].substring(0, items[3].indexOf("{")-1);
                return;
            }
            name = items[3];
        } catch (StringIndexOutOfBoundsException ex) {
            throw new DoesNotExist();
        } catch (ArrayIndexOutOfBoundsException ex) {
            throw new DoesNotExist();
        }
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public void setOnHeal(EventHandler<CombatHealEvent> handler) {
        // TODO Auto-generated method stub
    }

    @Override
    public void setOnDamage(EventHandler<CombatDamageEvent> handler) {
        // TODO Auto-generated method stub
    }

    @Override
    public void setOnCombatEnter(EventHandler<CombatEnterEvent> handler) {
        // TODO Auto-generated method stub
    }

    @Override
    public void setOnCombatExit(EventHandler<CombatExitEvent> handler) {
        // TODO Auto-generated method stub

    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Ability other = (Ability) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }

    public double getValueDone() {
        return valueDone;
    }

    public void setValueDone(double valueDone) {
        this.valueDone = valueDone;
    }

    public void incrementValueDone(double value) {
        valueDone += value;
    }

    @Override
    public String toString() {
        return String.format("Ability [name=%s]", name);
    }
}
