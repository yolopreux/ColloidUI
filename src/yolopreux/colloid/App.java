package yolopreux.colloid;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

public class App extends Application {

    TextField logPathField = new TextField("");
    TableView recountView;
    Button logPathButton;
    Button parseActButton;
    Recount recount = Recount.getInstance();
    TextArea textLog;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Colloid");
        primaryStage.setScene(createScene());
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public Scene createScene() {
        loadParams();

        System.out.println(System.getProperty("javafx.version"));

        recountView = new TableView();

        Scene scene = new Scene(new Group(), 1450, 360);
        scene.setFill(Color.GHOSTWHITE);

        Group root = (Group) scene.getRoot();
        root.getChildren().add(createLogPathPane());
        scene.getStylesheets().add("uicontrol/greeg-theme/win7glass.css");

        return scene;
    }


    public static void main(String[] args) {
        SwingMain.main(args);
    }

    @Override
    public void stop() throws Exception {
        recount.stop();
    }

    @SuppressWarnings("unchecked")
    protected TitledPane createLogPathPane() {
        TitledPane gridTitlePane = new TitledPane();

        Label logPathLabel = new Label("Log path: ");

        logPathField.setEditable(false);
        logPathField.setMinWidth(1300);

        logPathButton = new Button("...");
        parseActButton = new Button("Start");

        textLog = new TextArea("Combat" + "\n");
        textLog.setMinHeight(500);

        logPathButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (!recount.isRunning()) {
                    DirectoryChooser directoryChooser = new DirectoryChooser();
                    directoryChooser.setTitle("Choose combat log file path ...");
                    File file = directoryChooser.showDialog(null);
                    if (file != null) {
                        logPathField.setText(file.getPath());
                        recount.setCombatDirPath(file.getPath());
                        saveParams();
                    }
                } else {
                    recount.stop();
                }
            }
        });

        parseActButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (!logPathField.getText().isEmpty() && !recount.isRunning()) {
                    recount.setCombatDirPath(logPathField.getText()).run();
                    parseActButton.setText("Stop");
                    recount.setTextLog(textLog);
                } else {
                    recount.stop();
                    parseActButton.setText("Start");
                }
            }
        });

        @SuppressWarnings("rawtypes")
        TableColumn abilityCol = new TableColumn("Ability");
        @SuppressWarnings("rawtypes")
        TableColumn valueCol = new TableColumn("Value");
        recountView.getColumns().add(abilityCol);
        recountView.getColumns().add(valueCol);
        recountView.setEditable(false);

        gridTitlePane.autosize();
        GridPane grid = new GridPane();
        grid.setMinWidth(460);
        grid.setMinHeight(530);
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setPadding(new Insets(5, 5, 5, 5));
        grid.add(logPathLabel, 0, 0);
        grid.add(logPathField, 1, 0);
        grid.add(logPathButton, 2, 0);
        grid.add(parseActButton, 3, 0);
        grid.add(textLog, 1, 1);

        gridTitlePane.setText("Combat log path:");
        gridTitlePane.setContent(grid);

        return gridTitlePane;
    }

    public void loadParams() {
        Properties props = new Properties();
        InputStream inputStream = null;

        try {
            File file = new File("app.properties");
            inputStream = new FileInputStream(file);
        } catch (Exception e) {
            inputStream = null;
        }

        try {
            if (inputStream == null) {
                inputStream = getClass().getResourceAsStream("app.properties");
            }
            props.load(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }

        logPathField.setText(props.getProperty("combatLogPath", ""));
    }

    public void saveParams() {
        try {
            Properties props = new Properties();
            props.setProperty("combatLogPath", logPathField.getText());
            File file = new File("app.properties");
            OutputStream out = new FileOutputStream(file);
            props.store(out, "Colloid app properties");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
