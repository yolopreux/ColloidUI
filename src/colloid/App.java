package colloid;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import colloid.http.Peer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


public class App extends Application {

    Properties props;
    TextField logPathField = new TextField("");
    Button logPathButton;
    Button parseActButton;

    private Stage stage;

    private final double MINIMUM_WINDOW_WIDTH = 390.0;
    private final double MINIMUM_WINDOW_HEIGHT = 500.0;

    @Override
    public void start(Stage primaryStage) {
        loadParams();
        stage = primaryStage;
        //stage.setScene(createScene());

        showMain();
        primaryStage.show();
    }

    public Scene createScene() {
        loadParams();

        stage = new Stage();
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
        RecountApp.getInstance().stop();
        Peer.getInstance().stop();
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
            File file = new File("app.properties");
            OutputStream out = new FileOutputStream(file);
            props.store(out, "Colloid app properties");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class AppResource extends ResourceBundle {

        HashMap<String, Object> resource = new HashMap<String, Object>();

        @Override
        protected Object handleGetObject(String key) {
            return resource.get(key);
        }

        @Override
        public Enumeration<String> getKeys() {
            return (Enumeration<String>) resource.keySet();
        }

        public void set(String key, Object value) {
            resource.put(key, value);
        }

        public App getApp() {
            return (App) resource.get("app");
        }
    }

    private Initializable replaceSceneContent(String fxml, Scene scene) throws Exception {
        AppResource resource = new AppResource();
        resource.set("app", this);
        FXMLLoader loader = new FXMLLoader();
        InputStream in = App.class.getResourceAsStream(fxml);
        loader.setBuilderFactory(new JavaFXBuilderFactory());
        loader.setLocation(App.class.getResource(fxml));
        AnchorPane page;
        loader.setResources(resource);
        try {
            page = (AnchorPane) loader.load(in);
        } finally {
            in.close();
        }
        if (scene == null) {
            scene = new Scene(page);
            scene.getStylesheets().add("uicontrol/greeg-theme/win7glass.css");
            stage.setScene(scene);
        }
        stage.getScene().setRoot(page);
        stage.sizeToScene();

        return (Initializable) loader.getController();
    }

    private Initializable replaceSceneContent(String fxml) throws Exception {
        return replaceSceneContent(fxml, null);
    }

    public void showMain() {
        stage.setTitle("Colloid");
        stage.setMinWidth(MINIMUM_WINDOW_WIDTH);
        stage.setMinHeight(MINIMUM_WINDOW_HEIGHT);
        stage.setResizable(true);

        try {
            MainController controller = (MainController) replaceSceneContent("main.fxml");
            controller.init(this);
        } catch (Exception ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void showPopupTextLog() {
        stage = new Stage();
        stage.setTitle("Colloid combat");
        stage.setMinWidth(200);
        stage.setMinHeight(312);
        stage.setResizable(true);
        try {
            PopupTextLogController controller = (PopupTextLogController) replaceSceneContent("popupTextLog.fxml");
            stage.show();
        } catch (Exception ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Stage getStage() {
        return stage;
    }

    public Properties getProps() {
        if (props == null) {
            loadParams();
        }
        return props;
    }

    public void setProps(Properties props) {
        this.props = props;
    }
}
