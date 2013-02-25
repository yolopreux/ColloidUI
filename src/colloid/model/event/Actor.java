package colloid.model.event;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import colloid.model.event.Combat;
import colloid.model.event.Combat.Ability;
import colloid.model.event.Combat.Effect;
import colloid.model.event.Combat.Target;

public class Actor implements Combat.Actor {

//    HashMap<Date, CombatDamageEvent> damageEvent = new HashMap<Date, CombatDamageEvent>();
    ArrayList<CombatDamageEvent> damageEvent = new ArrayList<CombatDamageEvent>();

    @Override
    public boolean isPlayer() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public String name() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void compile() {
        // TODO Auto-generated method stub

    }

    @Override
    public void setOnHeal(CombatHealEvent event) {
        // TODO Auto-generated method stub

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


}
