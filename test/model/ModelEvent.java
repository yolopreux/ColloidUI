package model;

import java.util.Iterator;
import org.junit.Test;
import org.jmock.Expectations;
import org.jmock.Mockery;
import colloid.RecountApp;
import colloid.model.event.*;
import colloid.model.event.Character.DoesNotExist;
import colloid.model.event.Combat.Event;
import colloid.model.event.RecountLoop.InterruptLoopException;

import static org.junit.Assert.*;

public class ModelEvent {

    Mockery context = new Mockery();
    private static final String combatStr ="[23:23:12.901] [@Jolo] [@Jano] [Innervate {1104395005591552}] [ApplyEffect {836045448945477}: Heal {836045448945500}] (1714* force)";

    @Test
    public void createRecountApp() throws InterruptLoopException {

        final Combat.Recount recount = context.mock(Combat.Recount.class);

        context.checking(new Expectations() {{
            oneOf(recount).onUpdate();
            oneOf(recount).onStart();
            oneOf(recount).onStop();
        }});
    }

    @Test
    public void compileCharacter() {
        Actor actor = null;
        try {
            actor = new Actor(combatStr);
        } catch (DoesNotExist e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Target target = null;
        try {
            target = new Target(combatStr);
        } catch (DoesNotExist e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        assertEquals("@Jolo", actor.getName());
        assertEquals("@Jano", target.getName());
        final String logdata ="[23:45:12.782] [Thul Artillery Droid {670199581769728}:9572007456857] [@Yalo] [Twin-linked Railguns {2455114975543296}] [ApplyEffect {836045448945477}: Damage {836045448945501}] (346 energy {836045448940874}) <346>";
        Actor actor1 = null;
        try {
            actor1 = new Actor(logdata);
        } catch (DoesNotExist e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        assertEquals("Thul Artillery Droid", actor1.getName());
    }

    @Test
    public void compileCombatHealEvent() {

        Actor actor = null;
        try {
            actor = new Actor(combatStr);
        } catch (DoesNotExist e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        CombatHealEvent healEvent = new CombatHealEvent(actor, combatStr);
        assertEquals(1714.0, healEvent.getValue(), 0);

        final String logdataForce ="[23:23:12.901] [@Jolo] [@Jano] [Innervate {1104395005591552}] [ApplyEffect {836045448945477}: Heal {836045448945500}] (1714 force)";
        CombatHealEvent healEvent1 = new CombatHealEvent(actor, logdataForce);
        assertEquals(1714.0, healEvent1.getValue(), 0);

        final String logdata ="[23:23:12.901] [@Jolo] [@Jano] [Innervate {1104395005591552}] [ApplyEffect {836045448945477}: Heal {836045448945500}] (1714)";
        CombatHealEvent healEvent2 = new CombatHealEvent(actor, logdata);
        assertEquals(1714.0, healEvent2.getValue(), 0);
    }

    @Test
    public void compileCombatDamageEvent() {
        final String logdata ="[23:45:12.782] [Thul Artillery Droid {670199581769728}:9572007456857] [@Yalo] [Twin-linked Railguns {2455114975543296}] [ApplyEffect {836045448945477}: Damage {836045448945501}] (346 energy {836045448940874}) <346>";
        Actor actor = null;
        try {
            actor = new Actor(logdata);
        } catch (DoesNotExist e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        CombatEvent event = new CombatDamageEvent(actor, logdata);
        assertEquals(346.0, event.getValue(), 0);
    }

    @Test
    public void compileThreatDamageEvent() {
        final String logdata ="[23:45:12.782] [Thul Artillery Droid {670199581769728}:9572007456857] [@Yalo] [Twin-linked Railguns {2455114975543296}] [ApplyEffect {836045448945477}: Damage {836045448945501}] (346 energy {836045448940874}) <348>";
        Actor actor = null;
        try {
            actor = new Actor(logdata);
        } catch (DoesNotExist e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        CombatEvent event = new CombatThreatEvent(actor, logdata);
        assertEquals(348.0, event.getValue(), 0);
    }

    @Test
    public void addCombatDamageEvent() {
        final String logdata ="[23:45:12.782] [Thul Artillery Droid {670199581769728}:9572007456857] [@Yalo] [Twin-linked Railguns {2455114975543296}] [ApplyEffect {836045448945477}: Damage {836045448945501}] (346 energy {836045448940874}) <346>";
        Actor actor = null;
        try {
            actor = new Actor(logdata);
        } catch (DoesNotExist e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        RecountApp.getInstance();
        actor.setOnDamage(new Combat.EventHandler<Combat.Event>() {
            @Override
            public void handle(Combat.Event event) {
                assertEquals(346.0, event.getValue(), 0);
                assertTrue("instance of CombatDamageEvent", event instanceof CombatDamageEvent);
            }
        });
        actor.handleEvent(logdata);

    }

    @Test
    public void appRegisterActor() {
        try {
            Actor actor = new Actor(combatStr);
        } catch (DoesNotExist e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        RecountApp app = RecountApp.getInstance();
        try {
            app.registerActor(new Actor(combatStr));
        } catch (DoesNotExist e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            app.registerActor(new Actor(combatStr));
        } catch (DoesNotExist e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        final String logdata ="[23:45:15.154] [@Yalo:Kira Carsen {349412769398784}] [Thul Artillery Droid {670199581769728}:9572007456857] [Bleeding (Physical) {2071080474771712}] [ApplyEffect {836045448945477}: Damage {836045448945501}] (76 internal {836045448940876}) <76>";
        try {
            app.registerActor(new Actor(logdata));
        } catch (DoesNotExist e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        final String logdata1 ="[23:45:12.782] [Thul Artillery Droid {670199581769728}:9572007456857] [@Yalo] [Twin-linked Railguns {2455114975543296}] [ApplyEffect {836045448945477}: Damage {836045448945501}] (346 energy {836045448940874}) <346>";
        Actor droid = null;
        try {
            droid = new Actor(logdata1);
        } catch (DoesNotExist e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        app.registerActor(droid);
        final String logdata2 ="[23:45:16.972] [Thul Artillery Droid {670199581769728}:9572007456857] [@Yalo] [Charged Plating {811980747177984}] [ApplyEffect {836045448945477}: Damage {836045448945501}] (189 energy {836045448940874}) <189>";
        try {
            app.registerActor(new Actor(logdata2));
        } catch (DoesNotExist e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Iterator<Actor> iter = app.getActors().iterator();
        while(iter.hasNext()) {
            Actor act = iter.next();
            act.setOnDamage(new Combat.EventHandler<Combat.Event>() {
                @Override
                public void handle(Event event) {
                }
            });
            act.setOnHeal(new Combat.EventHandler<Combat.Event>() {
                @Override
                public void handle(Event event) {
                    // TODO Auto-generated method stub
                }
            });
            act.handleEvent(combatStr);
            act.handleEvent(combatStr);
            act.handleEvent(combatStr);
            act.handleEvent(logdata);
            act.handleEvent(logdata1);
            act.handleEvent(logdata2);
        }
        assertEquals(3, app.getActors().size());
        assertEquals(droid, app.getActorList().get(0));
    }

}
