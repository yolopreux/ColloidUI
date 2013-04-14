/**
 *  Colloid project
 *
 *  Combat log analyzer.
 *
 *  copyright: (c) 2013 by Darek <netmik12 [AT] gmail [DOT] com>
 *  license: BSD, see LICENSE for more details
 */
package colloid.model.event;

import colloid.model.event.Combat.EventHandler;


public class Effect implements Combat.Effect {

    protected String logdata;
    protected String name;

    public Effect(String logdata) throws DoesNotExist {
        this.logdata = logdata;
        compile();
    }

    public Effect(CombatEvent event) throws DoesNotExist {
        this.logdata = event.getLogdata();
        compile();
    }
    @Override
    public void compile() throws DoesNotExist {
        try {
            String[] items = logdata.substring(1).split("\\]\\s\\[");
            if (items[4] == null) {
                throw new DoesNotExist();
            }
            name = items[4].trim();
        } catch (StringIndexOutOfBoundsException ex) {
            throw new DoesNotExist();
        } catch (ArrayIndexOutOfBoundsException ex) {
            throw new DoesNotExist();
        }
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

    public String getLogdata() {
        return logdata;
    }

    public String getName() {
        return name;
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
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Effect other = (Effect) obj;
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        return true;
    }

}
