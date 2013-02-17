package colloid;

import java.net.URL;
import java.util.ResourceBundle;

import com.sun.glass.ui.MenuBar;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;


public class MainController extends AnchorPane implements Initializable {

    private App application;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public App getApplication() {
        return application;
    }

    public void setApplication(App application) {
        this.application = application;
    }
}