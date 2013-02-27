package colloid.model.event;

import colloid.model.event.Combat.EventHandler;

public class Ability implements Combat.Ability {

    @Override
    public void compile() {
        // TODO Auto-generated method stub

    }

    @Override
    public String name() {
        // TODO Auto-generated method stub
        return null;
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


}
