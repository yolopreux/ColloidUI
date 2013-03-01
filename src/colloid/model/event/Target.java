package colloid.model.event;

public class Target extends Character implements Combat.Target {

    public Target(String logdata) {
        super(logdata);
    }

    @Override
    public boolean isPlayer() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void compile() {
        try {
            String[] items = logdata.substring(1).split("\\]\\s\\[");
            name = items[2];
        } catch (StringIndexOutOfBoundsException ex) {
            ex.printStackTrace();
        }
    }
}
