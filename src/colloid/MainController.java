package colloid;


import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
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
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import colloid.model.event.Actor;
import colloid.model.event.Combat;
import colloid.model.event.CombatEvent;
import colloid.model.event.Fight;


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
    protected RecountApp recountApp = RecountApp.getInstance();
    @FXML
    TitledPane logPath;
    @FXML
    TitledPane recountLogPane;
    @FXML
    MenuItem menuItemClose;
    @FXML
    ListView<Actor> combatListView;
    @FXML
    TreeView<String> treeView;

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

        ObservableList<Actor> items = FXCollections.observableArrayList();
        combatListView.setItems(items);

        ObservableList<String> logItems = FXCollections.observableArrayList();
        textLog.setItems(logItems);

        recountApp.onUpdate(new Combat.EventHandler<CombatEvent>() {
            @Override
            public void handle(CombatEvent event) {
                updateActorCombat(event);
                createTreeView(recountApp.getFightList());
            }
        });
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

        if (!recountApp.isRunning()) {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("Choose combat log file path ...");
            File file = directoryChooser.showDialog(null);
            if (file != null) {
                logPathField.setText(file.getPath());
                recountApp.setCombatDirPath(file.getPath());
                application.props.setProperty("combatLogPath", logPathField.getText());
                application.saveParams();
                parseActButton.setDisable(false);
            }
        } else {
            recountApp.stop();
        }
    }

    public void toggleRecountAction(ActionEvent event) {
        if (!recountApp.isRunning()) {
            recountApp.setCombatDirPath(logPathField.getText()).run();
            recountApp.onUpdate(new Combat.EventHandler<CombatEvent>() {
                @Override
                public void handle(CombatEvent event) {
                    textLog.getItems().add(0, event.getLogdata());

                    recountApp.registerActor(new Actor(event.getLogdata()));
                    Iterator<Actor> iter = recountApp.getActors().iterator();
                    while(iter.hasNext()) {
                        Actor act = iter.next();
                        act.handleEvent(event.getLogdata());
                    }
                    combatListView.getItems().clear();
                    combatListView.getItems().addAll(recountApp.getActorList());

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

    private void createTreeView(ArrayList<Fight> fights) {
        TreeItem<String> root = new TreeItem<String>("Combat Fights");
        root.setExpanded(true);
        Iterator<Fight> iter = fights.iterator();
        while(iter.hasNext()) {
            Fight fight = iter.next();
            TreeItem<String> item = new TreeItem<String>(fight.info());
            Iterator<Actor> iterActor = fight.getActors().iterator();
            while(iterActor.hasNext()) {
                item.getChildren().add(new TreeItem<String>(iterActor.next().toString()));
            }
            root.getChildren().add(item);
        }
        treeView.setRoot(root);
    }

    private void updateActorCombat(CombatEvent event) {
        recountApp.registerActor(new Actor(event.getLogdata()));
        Iterator<Actor> iter = recountApp.getActors().iterator();
        while(iter.hasNext()) {
            Actor act = iter.next();
            act.handleEvent(event.getLogdata());
        }
    }
}