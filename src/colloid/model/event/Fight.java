package colloid.model.event;

import java.util.Date;

public class Fight {

    protected Date start;
    protected Date finish;

    public Date getFinish() {
        return finish;
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

    @Override
    public String toString() {
        return String.format("Fight [start=%s, finish=%s]", start, finish);
    }

}
