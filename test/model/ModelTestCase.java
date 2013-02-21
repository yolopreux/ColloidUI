package model;

import org.junit.Test;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import colloid.model.Recount;
import colloid.model.combat.ICombat;
import static org.junit.Assert.*;


public class ModelTestCase {

    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();

    @Mock ICombat combat;

    @Test
    public void CombatAddNewEvent() {
        // set up
        final String event = "new event";
        Recount.getInstance().setCombat(combat);

        context.checking(new Expectations() {{
            oneOf(combat).add(event);
        }});

        // execute
        Recount.getInstance().countCombat(event);
    }

    @Test
    public void EventValue() {
        fail("Not implemented");
    }
}
