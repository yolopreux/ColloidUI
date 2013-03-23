package colloid.model.control;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;

import javafx.scene.Node;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import colloid.model.event.Ability;
import colloid.model.event.Actor;
import colloid.model.event.Fight;
import colloid.model.event.Util;

abstract class FightTree {

    protected Fight fight;
    protected TreeItem<String> item;

    public FightTree(Fight fight) {
        this.fight = fight;
        item = new TreeItem<String>(fight.info());
    }

    protected void init(Comparator<Actor> comparator) {
        ArrayList<Actor> fightActors = new ArrayList<Actor>(fight.getActors());
        Collections.sort(fightActors, comparator);
        Iterator<Actor> iterActor = fightActors.iterator();

        while(iterActor.hasNext()) {
            Actor actor = iterActor.next();
            String vps = "";
            Date endTime = fight.getFinish();
            if (endTime == null) {
                endTime = new Date();
            }
            long duration = endTime.getTime() - fight.getStart().getTime();
            if (duration > 100) {
                vps = Util.valuePerSecond(valueDone(actor, fight), duration);
                double valueDone = valueDone(actor, fight);
                String info = String.format("%s: %s(%s)", actor.getName(), valueDone, vps);
                TreeItem<String> itemActor = new TreeItem<String>(info);

                ArrayList<Ability> actorAbilities = new ArrayList<Ability>(actor.getAbilities());
                Collections.sort(actorAbilities, actor.new AbilityValueDoneComparator());
                Iterator<Ability> iterAbility = actorAbilities.iterator();

                while (iterAbility.hasNext()) {
                    Ability ability = iterAbility.next();
                    if (ability.name() != null) {
                        final Node abilityIcon =  new ImageView(new Image(getClass().getResourceAsStream("/img/dark_embrace.jpg")));
                        itemActor.getChildren().add(new TreeItem<String>(ability.info(valueDone), abilityIcon));
                    }
                }
                item.getChildren().add(itemActor);
            }
        }
    }

    abstract protected double valueDone(Actor actor, Fight fight);

    public TreeItem<String> getItem() {
        return item;
    }

    public void setItem(TreeItem<String> item) {
        this.item = item;
    }
}
