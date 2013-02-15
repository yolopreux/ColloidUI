package yolopreux.colloid;

import java.io.File;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

public class App extends Application {

    TextField logPathField;

    @Override
    public void start(Stage primaryStage) {
        System.out.println(System.getProperty("javafx.version"));

        primaryStage.setTitle("Colloid");
        
        Scene scene = new Scene(new Group(), 500, 500);
        scene.setFill(Color.GHOSTWHITE);

        Group root = (Group) scene.getRoot();
        root.getChildren().add(createLogPathPane());
        scene.getStylesheets().add("uicontrol/greeg-theme/win7glass.css");

        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    protected TitledPane createLogPathPane() {
        TitledPane gridTitlePane = new TitledPane();

        Label logPathLabel = new Label("Log path: ");
        logPathField = new TextField("");
        logPathField.setMinWidth(400);
        Button logPathButton = new Button("...");
        logPathButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DirectoryChooser directoryChooser = new DirectoryChooser();
                directoryChooser.setTitle("Choose combat log file path ...");
                File file = directoryChooser.showDialog(null);
                if (file != null) {
                    logPathField.setText(file.getPath());
                }
            }
        });

        gridTitlePane.autosize();
        GridPane grid = new GridPane();
        grid.setMinWidth(400);
        grid.setVgap(4);
        grid.setHgap(4);
        grid.setPadding(new Insets(5, 5, 5, 5));
        grid.add(logPathLabel, 0, 0);
        grid.add(logPathField, 1, 0);
        grid.add(logPathButton, 2, 0);

        gridTitlePane.setText("Combat log path:");
        gridTitlePane.setContent(grid);

        return gridTitlePane;
    }
}
