/**
 *  Colloid project
 *
 *  Combat log analyzer.
 *
 *  copyright: (c) 2013 by Darek <netmik12 [AT] gmail [DOT] com>
 *  license: BSD, see LICENSE for more details
 */
package colloid.model.event;

public class Target extends Character implements Combat.Target {

    public Target(String logdata) throws DoesNotExist {
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
    public void compile() throws DoesNotExist {
        try {
            String[] items = logdata.substring(1).split("\\]\\s\\[");
            name = items[2];
        } catch (StringIndexOutOfBoundsException ex) {
            throw new DoesNotExist();
        } catch (ArrayIndexOutOfBoundsException ex) {
            throw new DoesNotExist();
        }
    }
}
