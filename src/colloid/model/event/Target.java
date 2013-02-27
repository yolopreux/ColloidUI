package colloid.model.event;

import colloid.model.event.Combat.EventHandler;

public class Target implements Combat.Target {

    @Override
    public boolean isPlayer() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void compile() {
        // TODO Auto-generated method stub

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
