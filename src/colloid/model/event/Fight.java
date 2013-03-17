/**
 *  Colloid project
 *
 *  Combat log analyzer.
 *
 *  copyright: (c) 2013 by Darek <netmik12 [AT] gmail [DOT] com>
 *  license: BSD, see LICENSE for more details
 */
package colloid.model.event;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Fight implements Comparable<Fight> {

    protected Date start;
    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    protected Date finish;
    protected HashSet<Actor> actors= new HashSet<Actor>();

    public Date getFinish() {
        return finish;
    }

    public HashSet<Actor> getActors() {
        return actors;
    }

    public void setActors(HashSet<Actor> actors) {
        this.actors = actors;
    }

    public void setFinish(Date finish) {
        this.finish = finish;
    }

    protected static Fight instance;

    public Fight() {
        start = new Date();
    }

    public static Fight current() {
        if (instance == null) {
            instance = new Fight();
        }

        return instance;
    }

    public static Fight create() {
        instance = new Fight();

        return instance;
    }

    public static void finish() {
        if (instance != null) {
            instance.setFinish(new Date());
            instance = null;
        }
    }

    public static boolean inFight() {
        if (instance == null) {

            return false;
        }

        return true;
    }

    public static void addActor(Actor actor) {
        if (instance == null) {
            Logger.getLogger(RecountLoop.class.getName()).log(Level.SEVERE, "Invalid fight", "");
        }
        instance.getActors().add(actor);
    }


    public long identificator() {
        return start.getTime();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((start == null) ? 0 : start.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Fight other = (Fight) obj;
        if (start == null) {
            if (other.start != null)
                return false;
        } else if (!start.equals(other.start))
            return false;
        return true;
    }

    public String info() {
        final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss",
                Locale.getDefault());

        if (finish == null) {
            return String.format("Fight start: %s", timeFormat.format(start));
        }
        Date diff = new Date(finish.getTime() - start.getTime());
        Calendar cal = Calendar.getInstance();
        cal.setTime(diff);
        return String.format("%s - %s, %s:%s min.", timeFormat.format(start),
                timeFormat.format(finish), cal.get(Calendar.MINUTE), cal.get(Calendar.SECOND));
    }

    @Override
    public String toString() {
        return String.format("Fight start: %s, finish: %s, \n actors: [%s]", start,
                finish, actors);
    }

    @Override
    public int compareTo(Fight o) {
        if (start.getTime() > o.getStart().getTime()) {
            return 1;
        }
        return -1;
    }

    public class ActorDamageDoneComparator implements Comparator<Actor> {

        @Override public int compare(Actor actor1, Actor actor2) {
            if (actor1.getDamageDone() < actor2.getDamageDone()) {
                return 1;
            }
            return -1;
        }
    }

}
