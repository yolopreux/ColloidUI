package colloid;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import colloid.model.Recount;


public class App extends Application {

    Properties props;
    TextField logPathField = new TextField("");
    Button logPathButton;
    Button parseActButton;
    Recount recount = Recount.getInstance();

    private Stage stage;
    private final double MINIMUM_WINDOW_WIDTH = 390.0;
    private final double MINIMUM_WINDOW_HEIGHT = 500.0;

    @Override
    public void start(Stage primaryStage) {
        loadParams();
        stage = primaryStage;
        stage.setTitle("Colloid");
        //stage.setScene(createScene());
        stage.setMinWidth(MINIMUM_WINDOW_WIDTH);
        stage.setMinHeight(MINIMUM_WINDOW_HEIGHT);
        stage.setResizable(true);
        showMain();

        primaryStage.show();
    }

    /**
     * Create scene
     * @deprecated
     */
    public Scene createScene() {
        loadParams();

        stage = new Stage(); 
//        Scene scene = new Scene(new Group(), 1450, 360);
//        scene.setFill(Color.GHOSTWHITE);

//        Group root = (Group) scene.getRoot();
        showMain();
        
        stage.getScene().getStylesheets().add("uicontrol/greeg-theme/win7glass.css");

        return stage.getScene();
    }


    public static void main(String[] args) {
        //SwingMain.main(args);
        launch(args);
    }

    @Override
    public void stop() throws Exception {
        recount.stop();
        stage.close();
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

        this.props = props;
    }

    public void saveParams() {
        try {
            if (props == null) {
                props = new Properties();
            }
            props.setProperty("combatLogPath", logPathField.getText());
            File file = new File("app.properties");
            OutputStream out = new FileOutputStream(file);
            props.store(out, "Colloid app properties");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Initializable replaceSceneContent(String fxml) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        InputStream in = App.class.getResourceAsStream(fxml);
        loader.setBuilderFactory(new JavaFXBuilderFactory());
        loader.setLocation(App.class.getResource(fxml));
        AnchorPane page;
        try {
            page = (AnchorPane) loader.load(in);
        } finally {
            in.close();
        }
        // store the stage height in case the user has resized the window
        double stageWidth = stage.getWidth();
        if (!Double.isNaN(stageWidth)) {
            stageWidth -= (stage.getWidth() - stage.getScene().getWidth());
        }
        double stageHeight = stage.getHeight();
        if (!Double.isNaN(stageHeight)) {
            stageHeight -= (stage.getHeight() - stage.getScene().getHeight());
        }
//        Scene scene = stage.getScene();
//        if (scene == null) {
        Scene scene = new Scene(page);
        if (!Double.isNaN(stageWidth)) {
            page.setPrefWidth(stageWidth);
        }
        if (!Double.isNaN(stageHeight)) {
            page.setPrefHeight(stageHeight);
        }
        scene.getStylesheets().add("uicontrol/greeg-theme/win7glass.css");
        stage.setScene(scene);
//        } else {
//            stage.getScene().setRoot(page);
//        }
        stage.sizeToScene();
        return (Initializable) loader.getController();
    }

    private void showMain() {
        try {
            MainController controller = (MainController) replaceSceneContent("main.fxml");
            controller.init(this);
        } catch (Exception ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
