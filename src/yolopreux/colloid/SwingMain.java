package yolopreux.colloid;

import java.awt.Dimension;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class SwingMain {

    private static void initAndShowGUI() {
        JFrame frame = new JFrame("Colloid Combat Log");
        final JFXPanel fxPanel = new JFXPanel();
        frame.add(fxPanel);
        frame.setAlwaysOnTop(true);
        frame.setMinimumSize(new Dimension(1100, 200));
        frame.setVisible(true);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                initFX(fxPanel);
            }
        });
    }

    private static void initFX(JFXPanel fxPanel) {
        // This method is invoked on JavaFX thread
        Scene scene = new App().createScene();
        fxPanel.setScene(scene);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                initAndShowGUI();
            }
        });
    }

}
