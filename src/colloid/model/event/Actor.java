package colloid.model.event;

import colloid.model.event.Combat;

public class Actor implements Combat.Actor {

//    HashMap<Date, CombatDamageEvent> damageEvent = new HashMap<Date, CombatDamageEvent>();
//    ArrayList<CombatDamageEvent> damageEvent = new ArrayList<CombatDamageEvent>();
    protected String name;
    protected String logdata;

    public Actor(String logdata) {
        this.logdata = logdata;
        compile();
    }

    @Override
    public boolean isPlayer() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void compile() {
        String[] items = logdata.substring(1).split("\\]\\s\\[");
        name = items[1];
    }

    @Override
    public void setOnHeal(CombatHealEvent event) {
        event.handle(event);

    }

    @Override
    public void setOnDamage(CombatDamageEvent event) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setOnCombatEnter(CombatEnterEvent event) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setOnCombatExit(CombatExitEvent event) {
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
        Actor other = (Actor) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }


}
