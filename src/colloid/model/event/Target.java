package colloid.model.event;

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
