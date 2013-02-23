package colloid.model.combat;

import java.util.HashMap;
import colloid.model.Recount;
import colloid.model.combat.CombatEvent;


public class Combat implements ICombat {

    Recount recount;

    @Override
    public String toString() {
        return "Combat [event=" + event + ", data=" + data + "]";
    }

    CombatEvent event;

    HashMap<String, HashMap<String, Double>> data;

    public Combat() {
        data = new HashMap<String, HashMap<String, Double>>();
    }

    public Combat(Recount recount) {
        this.recount = recount;
        data = new HashMap<String, HashMap<String, Double>>();
    }


    public Combat add(String newEvent) {
        String[] items = newEvent.substring(1).split("\\]\\s\\[");
        if (items != null) {
            event = new CombatEvent(items);
        }

        return this;
    }

    @Override
    public void recount(Recount.UpdateRecountLog updateRecountLog) {
        if (event.effect == null) {
            return;
        }
        if (event.effect.isEnterCombat()) {
            if (!data.containsKey(event.actor.name)) {
                data.put(event.actor.name, new HashMap<String, Double>());
            }
        }
        if (event.effect.isDamage()) {
            if (!data.containsKey(event.actor.name)) {
                data.put(event.actor.name, new HashMap<String, Double>());
            }
            if (!data.get(event.actor.name).containsKey("damage")) {
                data.get(event.actor.name).put("damage",
                        event.effect.getValue());
            } else {
                double damage = data.get(event.actor.name).get("damage")
                        + event.effect.getValue();
                data.get(event.actor.name).put("damage", damage);
            }
        }
        if (event.effect.isHeal()) {
            if (!data.containsKey(event.actor.name)) {
                data.put(event.actor.name, new HashMap<String, Double>());
            }
            if (!data.get(event.actor.name).containsKey("heal")) {
                data.get(event.actor.name).put("heal", event.effect.getValue());
            } else {
                double heal = data.get(event.actor.name).get("heal")
                        + event.effect.getValue();
                data.get(event.actor.name).put("heal", heal);
            }
        }
        if (recount.getRecountLog() == null) {
            return;
        }

        String text = "Damage:" + "\n";
        for (String key: data.keySet()) {
            text = text + "Actor: " + key + "\n";
            if (data.get(key).containsKey("damage")) {
                text = text + "Damage: " + Double.toString(data.get(key).get("damage")) + "\n"; 
            }
        }
        text = text + "Damage:" + "\n";
        for (String key: data.keySet()) {
            text = text + "Actor: " + key + "\n";
            if (data.get(key).containsKey("heal")) {
                text = text + "Heal: " + Double.toString(data.get(key).get("heal")) + "\n"; 
            }
        }
        updateRecountLog.update(text);
    }

    public CombatEvent getCombatEvent() {
        return event; 
    }
}
