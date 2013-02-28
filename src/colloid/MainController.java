package colloid;


import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.collections.ObservableSet;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import colloid.model.Recount;
import colloid.model.event.Combat;
import colloid.model.event.CombatEvent;


public class MainController extends AnchorPane implements Initializable {

    private final int START_TAIL_SIZE = 3000;
    @FXML
    TextField combatLogPath;
    @FXML
    TextField logPathField;
    @FXML
    Button logPathButton;
    @FXML
    Button parseActButton;
    @FXML
    ListView<String> textLog;
    @FXML
    TextArea recountLog;
    @FXML
    Button resetCombatButton;
    Recount recount = Recount.getInstance();
    protected RecountApp recountApp = RecountApp.getInstance();
    @FXML
    TitledPane logPath;
    @FXML
    TitledPane recountLogPane;
    @FXML
    MenuItem menuItemClose;
    @FXML
    ListView<String> combatListView;

    private App application;

    public void init(App application) {
        setApplication(application);
        logPathField.setText(application.props.getProperty("combatLogPath", ""));
        if (!logPathField.getText().isEmpty()) {
            parseActButton.setDisable(false);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        textLog.setEditable(false);
        recountLog.setEditable(false);

        ObservableList<String> items = FXCollections.observableArrayList();
        combatListView.setItems(items);
        recount.setCombatItems(items);

        ObservableList<String> logItems = FXCollections.observableArrayList();
        textLog.setItems(logItems);
        recount.setCombatItems(items);
    }

    public App getApplication() {
        return application;
    }

    public void setApplication(App application) {
        this.application = application;
    }

    public void resetCombat(ActionEvent event) {

    }

    public void closeAction(ActionEvent event) {
        try {
            application.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void chooseCombatDirPathAction(ActionEvent event) {

        if (!Recount.getInstance().isRunning()) {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("Choose combat log file path ...");
            File file = directoryChooser.showDialog(null);
            if (file != null) {
                logPathField.setText(file.getPath());
                recount.setCombatDirPath(file.getPath());
                application.props.setProperty("combatLogPath", logPathField.getText());
                application.saveParams();
                parseActButton.setDisable(false);
            }
        } else {
            recount.stop();
        }
    }

    public void toggleRecountAction(ActionEvent event) {
        if (!recountApp.isRunning()) {
            recountApp.setCombatDirPath(logPathField.getText()).run();
            recountApp.onUpdate(new Combat.EventHandler<CombatEvent>() {
                @Override
                public void handle(CombatEvent event) {
                    textLog.getItems().add(event.getLogdata());
                }
            });
            parseActButton.setText("Stop");
        } else {
            recountApp.stop();
            parseActButton.setText("Start");
        }
    }

    public void switchPaneAction(MouseEvent event) {
    }

    public void openTextLogPopupAction(ActionEvent event) {
        application.showPopupTextLog();
    }
}