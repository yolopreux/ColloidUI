/**
 *  Colloid project
 *
 *  Combat log analyzer.
 *
 *  copyright: (c) 2013 by Darek <netmik12 [AT] gmail [DOT] com>
 *  license: BSD, see LICENSE for more details
 */
package colloid;


import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import colloid.http.Peer;
import colloid.model.event.Actor;
import colloid.model.event.Combat;
import colloid.model.event.CombatEvent;
import colloid.model.event.Util;


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
    MenuItem menuHelpHowto;
    @FXML
    ListView<Actor> combatListView;
    @FXML
    TreeView<String> treeView;
    @FXML
    TextArea helptext;
    @FXML
    Tab howToUse;
    @FXML
    TabPane combatTabPane;
    @FXML
    RadioButton isPeer;

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
        if (recountApp.isRunning()) {
            parseActButton.setText("Stop");
        }
        textLog.setEditable(false);
        recountLog.setEditable(false);

        ObservableList<Actor> items = FXCollections.observableArrayList();
        combatListView.setItems(items);

        ObservableList<String> logItems = FXCollections.observableArrayList();
        textLog.setItems(logItems);

        recountApp.onUpdate(new Combat.EventHandler<CombatEvent>() {
            @Override
            public void handle(CombatEvent event) {
                treeView.setRoot(Util.rootTreeView(recountApp.getFightList()));
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
            Peer.getInstance().stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void howTouseAction(ActionEvent event) {
        recountLogPane.setExpanded(true);
        combatTabPane.getSelectionModel().clearAndSelect(3);
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
                    combatListView.getItems().clear();
                    combatListView.getItems().addAll(recountApp.getActorList());
                    if (Peer.getInstance().isRunning()) {
                        Peer.getInstance().send(event.getLogdata());
                    }
                }
            });
            if (isPeer.isSelected() && !Peer.getInstance().isRunning()) {
                Peer.getInstance().run();
            }
            parseActButton.setText("Stop");
        } else {
            recountApp.stop();
            if (Peer.getInstance().isRunning()) {
                Peer.getInstance().stop();
            }
            parseActButton.setText("Start");
        }
    }

    public void switchPaneAction(MouseEvent event) {
    }

    public void openTextLogPopupAction(ActionEvent event) {
        application.showPopupTextLog();
    }
}