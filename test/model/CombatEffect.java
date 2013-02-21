package model;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;

import colloid.model.Recount;
import colloid.model.combat.CombatEvent;
import colloid.model.combat.ICombatEntity;

public class CombatEffect {

    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();

    @Mock 
    ICombatEntity effect;

    @Test
    public void ParseCombatValue() {

        String combatStr = "[23:23:12.901] [@Jolo] [@Jolo] [Innervate {1104395005591552}] [ApplyEffect {836045448945477}: Heal {836045448945500}] (1714*)";
        context.checking(new Expectations(){{
            oneOf(effect).parse();
        }});

        CombatEvent event = new CombatEvent();
        effect.fromString(combatStr);
        event.setEffect(effect);
        Recount.getInstance().countCombat(combatStr);
    }

}
