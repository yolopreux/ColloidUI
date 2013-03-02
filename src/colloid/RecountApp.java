package colloid;

import javafx.collections.ObservableList;
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
    }

    /**
     * Executes in Fx application thread
     */
    @Override public void onUpdate() {
    }

    @Override
    public void onStop() {
    }

    public void setTextLog(final ObservableList<String> textLog) {
        this.textLog = textLog;
    }


}
