package colloid;

import javafx.collections.ObservableList;
import colloid.model.Recount;
import colloid.model.event.RecountLoop;

public class RecountApp  extends RecountLoop {

    ObservableList<String> textLog;
    private static RecountApp instance;

    protected RecountApp() {
    }

    public static RecountApp getInstance() {
        if (instance == null) {
            instance = new RecountApp();
        }

        return instance;
    }

    @Override
    public void onStart() {

        System.out.println("onStart");
    }

    @Override
    public void onUpdate() {
        System.out.println("onUpdae");
    }

    @Override
    public void onStop() {
        System.out.println("onStop");
    }

    public void setTextLog(final ObservableList<String> textLog) {
        this.textLog = textLog;
    }
}
