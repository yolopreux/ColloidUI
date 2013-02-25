package model;

import org.junit.Test;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;

import colloid.model.event.Combat;
import colloid.model.event.Target;
import colloid.model.event.Combat.Ability;
import colloid.model.event.Combat.Actor;
import colloid.model.event.Combat.Effect;
import colloid.model.combat.ICombat;
import static org.junit.Assert.*;

public class ModelEvent {

    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();

    @Mock ICombat combat;

    @Test
    public void addCombat() {
        fail();
    }
}
