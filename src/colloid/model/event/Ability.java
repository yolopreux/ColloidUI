/**
 *  Colloid project
 *
 *  Combat log analyzer.
 *
 *  copyright: (c) 2013 by Darek <netmik12 [AT] gmail [DOT] com>
 *  license: BSD, see LICENSE for more details
 */
package colloid.model.event;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import colloid.model.event.CombatEvent.EventType;
import colloid.model.event.DoesNotExist;
import colloid.model.event.Combat.EventHandler;

public class Ability implements Combat.Ability {

    protected String logdata;
    protected String name;
    protected double valueDone = 0;
    protected double healDone = 0;
    protected double damageDone = 0;
    protected double threatDone = 0;
    protected EventType etype;
    protected Node icon;

    public Ability(String logdata) throws DoesNotExist {
        this.logdata = logdata;
        compile();
        setIcon();
    }

    public Ability(CombatEvent event) throws DoesNotExist {
        this.logdata = event.getLogdata();
        compile();
        setIcon();
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

    protected void setIcon() {
        String iconPath = String.format("/img/small/%s.jpg", name.replaceAll(" ", "_").toLowerCase());
        try {
            icon = new ImageView(new Image(getClass().getResourceAsStream(iconPath)));
        } catch (NullPointerException ex) {
            icon = null;
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

    public double getHealDone() {
        return healDone;
    }

    /**
     * @TODO move value done into damage done
     */
    public double getDamageDone() {
        return valueDone;
    }

    public void setValueDone(double valueDone) {
        this.valueDone = valueDone;
    }

    public void incrementValueDone(double value) {
        valueDone += value;
    }

    public void incrementHealDone(double value) {
        healDone += value;
    }

    public void incrementDamageDone(double value) {
        damageDone += value;
    }


    @Override
    public String toString() {
        return String.format("Ability [name=%s]", name);
    }

    public String info() {
        return String.format("%s: %s", name, valueDone);
    }

    public String info(double totalValue) {
        return String.format("%s: %s(%s%s)", name, valueDone, Util.percentTotal(valueDone, totalValue), "%");
    }

    public String healInfo(double totalValue) {
        return String.format("%s: %s(%s%s)", name, healDone, Util.percentTotal(healDone, totalValue), "%");
    }

    public EventType getEtype() {
        return etype;
    }

    public Node getIcon() {
        return icon;
    }

}
