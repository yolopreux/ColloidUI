package colloid.model.event;

import colloid.RecountApp;
import colloid.model.event.Combat.EventHandler;


public class Character implements Combat.Character {

    @Override
    public void compile() {
        // TODO Auto-generated method stub
    }

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
    public void setOnHeal(EventHandler<CombatHealEvent> handler) {
        String logdata = RecountApp.getInstance().getCurrentLogadata();
        if (logdata != null && logdata.contains("Heal")) {
            CombatHealEvent event = new CombatHealEvent(this, logdata);
            handler.handle(event);
        }
    }

    @Override
    public void setOnDamage(EventHandler<CombatDamageEvent> handler) {
        String logdata = RecountApp.getInstance().getCurrentLogadata();
        if (logdata != null && logdata.contains("Damage")) {
            CombatDamageEvent event = new CombatDamageEvent(this, logdata);
            handler.handle(event);
        }
    }

    @Override
    public void setOnCombatEnter(EventHandler<CombatEnterEvent> handler) {
        String logdata = RecountApp.getInstance().getCurrentLogadata();
        if (logdata != null && logdata.contains("CombatEnter")) {
            CombatEnterEvent event = new CombatEnterEvent(this, logdata);
            handler.handle(event);
        }
    }

    @Override
    public void setOnCombatExit(EventHandler<CombatExitEvent> handler) {
        String logdata = RecountApp.getInstance().getCurrentLogadata();
        if (logdata != null && logdata.contains("CombatExit")) {
            CombatExitEvent event = new CombatExitEvent(this, logdata);
            handler.handle(event);
        }
    }

}
