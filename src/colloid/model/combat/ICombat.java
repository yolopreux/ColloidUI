package colloid.model.combat;

import colloid.model.IRecount;

public interface ICombat {

    public ICombat add(String data);

    public void recount(IRecount.UpdateRecountLog updateRecountLog);
}
