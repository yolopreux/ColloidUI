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
import colloid.model.Recount;
import colloid.model.event.Combat;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.collections.ObservableSet;
import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.WindowEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.DragEvent;

public class PopupTextLogController extends AnchorPane implements Initializable {
    static AppResource resource;
    JFrame frame;
    @FXML
    ListView<String> popupTextLog;
    @FXML
    Button moveMe;
    @FXML
    Button close;
    @FXML
    Button resize;

    @Override
    public void initialize(URL url, ResourceBundle bundleResource) {

        resource = (AppResource) bundleResource;
        RecountApp.getInstance().setObservable(new Combat.ObservableListString() {
            @Override  public ObservableSet<String> getSet() {
                return null;
            }

            @Override public ObservableMap<String, String> getMap() {
                return null;
            }

            @Override public ObservableList<String> getList() {
                return popupTextLog.getItems();
            }
        });

        resource.getApp().getStage().setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                if (frame != null) {
                    frame.dispose();
                }
                resource.getApp().getStage().setOpacity(1f);
                resource.getApp().showMain();
            }
        });

        moveMe.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (frame == null) {
                    return;
                }
                java.awt.Point location = frame.getLocationOnScreen();
                location.setLocation(event.getScreenX(), event.getScreenY());
                frame.setLocation(location);
            }
        });

        popupTextLog.setOnDragExited(new EventHandler<DragEvent>(){
            @Override
            public void handle(DragEvent e) {
                System.out.println(e);
            }
        });

        resize.setOnMouseDragged(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                if (frame == null) {
                    return;
                }

                java.awt.Dimension size = frame.getSize();
                java.awt.Point location = frame.getLocationOnScreen();
                double width = event.getScreenX() - location.getX();
                double height = event.getScreenY() - location.getY();

                if (width > 5 && height > 5) {
                    size.setSize(width, height);
                }
                frame.setSize(size);
            }
        });
        if (!GraphicsEnvironment.isHeadless()) {
            resource.getApp().getStage().setOpacity(0.0f);
            showSwing();
        }
    }

    private void initAndShowGUI() {
        frame = new JFrame("Colloid Combat Log");
        final JFXPanel fxPanel = new JFXPanel();
        frame.add(fxPanel);
        frame.setAlwaysOnTop(true);
        frame.setSize(new java.awt.Dimension(150, 180));
        frame.setLocation(new java.awt.Point(10, 600));
        frame.setUndecorated(true);
        frame.setOpacity(0.8f);
        frame.setVisible(true);

        frame.addMouseWheelListener(new MouseWheelListener() {

            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
            }
        });

        frame.addWindowListener(new WindowListener() {
            @Override public void windowOpened(java.awt.event.WindowEvent e) {
            }

            @Override public void windowIconified(java.awt.event.WindowEvent e) {
            }

            @Override
            public void windowDeiconified(java.awt.event.WindowEvent e) {
            }

            @Override public void windowDeactivated(java.awt.event.WindowEvent e) {
            }

            @Override public void windowClosing(java.awt.event.WindowEvent e) {
                frame.dispose();
            }

            @Override public void windowClosed(java.awt.event.WindowEvent e) {
                Platform.runLater(new Runnable() {
                    @Override public void run() {
                        resource.getApp().showMain();
                        resource.getApp().getStage().setOpacity(1f);
                    }
                });
            }

            @Override public void windowActivated(java.awt.event.WindowEvent e) {
            }
        });

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                initFX(fxPanel);
            }
        });
    }

    public void close() {
        Platform.runLater(new Runnable() {
            @Override public void run() {
                resource.getApp().getStage().close();
            }
        });
    }

    private static void initFX(JFXPanel fxPanel) {
        fxPanel.setScene(resource.getApp().getStage().getScene());
    }

    public void showSwing() {
        if (SwingUtilities.isEventDispatchThread()) {
            initAndShowGUI();
        } else {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    showSwing();
                }
            });
        }
    }

    public void closeAction(ActionEvent event) {
        Platform.runLater(new Runnable() {
            @Override public void run() {
                if (frame != null) {
                    frame.dispose();
                    resource.getApp().getStage().close();
                }
                resource.getApp().getStage().setOpacity(1f);
                resource.getApp().showMain();
            }
        });
    }
}
