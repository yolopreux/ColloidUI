package colloid;

import java.awt.GraphicsEnvironment;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowListener;
import java.net.URL;
import java.util.ResourceBundle;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import colloid.App.AppResource;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class DotController extends AnchorPane implements Initializable {

    final RecountApp recountApp = RecountApp.getInstance();
    static AppResource resource;
    PopupWindow win;

    @FXML
    ImageView imgView;
    @FXML
    Button moveMe;
    @FXML
    Button close;
    @FXML
    Button resize;

    @Override
    public void initialize(URL url, ResourceBundle bundleResource) {

        resource = (AppResource) bundleResource;
        win = new PopupWindow(bundleResource, moveMe, resize);
    }

    public void closeAction(ActionEvent event) {
        win.getFrame().dispose();
    }
}
