package colloid.model;

public interface IRecount {

    public interface UpdateTextLog {
        void update(String text);
        void insert(String text);
    }

    public interface UpdateRecountLog {
        void update(String text);
    }
}
