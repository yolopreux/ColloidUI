package colloid;

import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.ResourceBundle;

import colloid.model.Recount;

import com.sun.glass.ui.MenuBar;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;


public class MainController extends AnchorPane implements Initializable {

    @FXML
    TextField combatLogPath;
    @FXML
    TextField logPathField;
    @FXML
    Button logPathButton;
    @FXML
    Button parseActButton;
//    @FXML
//    TextArea textLog;
    @FXML
    Button resetCombatButton;
    Recount recount = Recount.getInstance();

    
    private App application;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        resetCombatButton.setOnMouseClicked(new EventHandler<Event>() {
//
//            @Override
//            public void handle(Event event) {
//                resetCombat(event);
//            }
//            
//        });
    }

    public App getApplication() {
        return application;
    }

    public void setApplication(App application) {
        this.application = application;
    }
    
    public void resetCombat(Event event) {
        System.out.println(event);
    }
}