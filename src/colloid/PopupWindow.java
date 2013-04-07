package colloid;

import java.awt.GraphicsEnvironment;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowListener;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import colloid.App.AppResource;

public class PopupWindow {

    static AppResource resource;
    JFrame frame;

    Button moveMe = null;
    Button close = null;
    Button resize = null;

    public PopupWindow(ResourceBundle bundleResource, Button moveMe, Button resize) {
        this.moveMe = moveMe;
        this.resize = resize;
        initialize(bundleResource);
    }

    public PopupWindow(ResourceBundle bundleResource, Button resize) {
        this.resize = resize;
        initialize(bundleResource);
    }

    public PopupWindow(ResourceBundle bundleResource) {
        initialize(bundleResource);
    }

    public void initialize(ResourceBundle bundleResource) {
        resource = (AppResource) bundleResource;
        if (!GraphicsEnvironment.isHeadless()) {
            showSwing();
        }

        if (moveMe != null) {
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
        }

        if (resize != null) {
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
        }
    }

    private static void initFX(JFXPanel fxPanel) {
        if (resource.getObject("scene") != null) {
            fxPanel.setScene((Scene) resource.getObject("scene"));
        } else {
            fxPanel.setScene(resource.getApp().getStage().getScene());
        }
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

    private void initAndShowGUI() {
        frame = new JFrame("Dot timer");
        final JFXPanel fxPanel = new JFXPanel();
        frame.add(fxPanel);
        frame.setAlwaysOnTop(true);
        frame.setSize(new java.awt.Dimension(490, 65));
        frame.setLocation(new java.awt.Point(180, 550));
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

    public JFrame getFrame() {
        return frame;
    }

    public void setMoveMe(Button moveMe) {
        this.moveMe = moveMe;
    }

    public void setClose(Button close) {
        this.close = close;
    }

    public void setResize(Button resize) {
        this.resize = resize;
    }
}
