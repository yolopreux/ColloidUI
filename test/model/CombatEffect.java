package model;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.CoreMatchers.*;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
//import static org.junit.Assert.*;

import colloid.model.IRecount;
import colloid.model.Recount;
import colloid.model.combat.Combat;
import colloid.model.combat.CombatEvent;
import colloid.model.combat.Effect;
import colloid.model.combat.ICombatEntity;

public class CombatEffect {

    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();

    @Mock 
    ICombatEntity effect;
    private static final String combatStr ="[23:23:12.901] [@Jolo] [@Jolo] [Innervate {1104395005591552}] [ApplyEffect {836045448945477}: Heal {836045448945500}] (1714* force)";
    private static final String damagCombatStr = "[21:35:52.750] [@Jalo] [Operations Training Target MK-5 {2816265890562048}:7284000006920] [Shocked (Force) {808235535696133}] [ApplyEffect {836045448945477}: Damage {836045448945501}] (651 internal {836045448940876}) <1955>";

    @Before
    public void setUp() {
    }

    @Test
    public void parseCombatValue() {
 
        context.checking(new Expectations(){{
            oneOf(effect).parse();
        }});

        CombatEvent event = new CombatEvent();
        effect.fromString(combatStr);
        event.setEffect(effect);
        Recount recount = Recount.getInstance();
        recount.countCombat(combatStr, new IRecount.UpdateRecountLog() {
            @Override
            public void update(String text) {
            }
        });
    }

    @Test
    public void countHealValue() {
        Recount recount = Recount.getInstance();
        recount.countCombat(combatStr, new IRecount.UpdateRecountLog() {
            @Override
            public void update(String text) {
            }
        });
        Combat combat = (Combat) recount.getCombat();
        Effect effect = combat.getCombatEvent().getEffect();
        assertThat(effect.getLogData(), containsString("Heal"));
        assertThat(effect.getValue(), notNullValue());
        assertThat(Double.toString(effect.getValue()), is(equalTo("1714.0")));
    }

    @Test
    public void countDamagevalue() {
        Recount recount = Recount.getInstance();
        recount.countCombat(damagCombatStr, new IRecount.UpdateRecountLog() {
            @Override
            public void update(String text) {
            }
        });
        Combat combat = (Combat) recount.getCombat();
        Effect effect = combat.getCombatEvent().getEffect();
        assertThat(effect.getLogData(), containsString("Damage"));
        assertThat(effect.getValue(), notNullValue());
        assertThat(Double.toString(effect.getValue()), is(equalTo("651.0")));
    }

}
