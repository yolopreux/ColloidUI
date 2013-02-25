package model;

import org.junit.Test;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;

import colloid.RecountApp;
import colloid.model.event.Combat;
import colloid.model.event.RecountLoop;
import colloid.model.event.Target;
import colloid.model.event.Combat.Ability;
import colloid.model.event.Combat.Actor;
import colloid.model.event.Combat.Effect;
import colloid.model.event.RecountLoop.InterruptLoopException;
import colloid.model.combat.ICombat;
import static org.junit.Assert.*;

public class ModelEvent {

    Mockery context = new Mockery();

    @Test
    public void createRecountApp() throws InterruptLoopException {

        final Combat.Recount recount = context.mock(Combat.Recount.class);

        context.checking(new Expectations() {{
            oneOf(recount).onUpdate();
            oneOf(recount).onStart();
            oneOf(recount).onStop();
        }});
    }
}
