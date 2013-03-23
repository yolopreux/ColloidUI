/**
 *  Colloid project
 *
 *  Combat log analyzer.
 *
 *  copyright: (c) 2013 by Darek <netmik12 [AT] gmail [DOT] com>
 *  license: BSD, see LICENSE for more details
 */
package colloid;

import java.awt.GraphicsEnvironment;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ResourceBundle;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import colloid.App.AppResource;
import colloid.model.control.DamageFightTree;
import colloid.model.event.Actor;
import colloid.model.event.Combat;
import colloid.model.event.CombatEvent;
import colloid.model.event.Fight;
import colloid.model.event.Util;
import colloid.model.event.Ability;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.WindowEvent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.DragEvent;

public class PopupTextLogController extends AnchorPane implements Initializable {

    final RecountApp recountApp = RecountApp.getInstance();
    static AppResource resource;
    ArrayList<Actor> historyLog = new ArrayList<Actor>();
    Fight lastFight;

    JFrame frame;
    @FXML
    ListView<String> popupTextLog;
    @FXML
    Button moveMe;
    @FXML
    Button close;
    @FXML
    Button resize;
    @FXML
    TreeView<String> treeView;
    @FXML
    TreeView<String> combatTreeView;
    @FXML
    TreeView<String> settingsTreeView;
    @FXML
    TreeView<String> combatDamageTreeView;
    @FXML
    TreeView<String> combatHealTreeView;

    TreeItem<ArrayList<Actor>> rootItem = new TreeItem<ArrayList<Actor>>();

    @Override
    public void initialize(URL url, ResourceBundle bundleResource) {
        settingsTreeView.setRoot(new TreeItem<String>("Info"));
        settingsTreeView.getRoot().setExpanded(true);
        settingsTreeView.getRoot().getChildren().add(0, new TreeItem<String>(recountApp.sysInfo.allocatedMemInfo()));
        settingsTreeView.getRoot().getChildren().add(1, new TreeItem<String>(recountApp.sysInfo.maxMemInfo()));
        settingsTreeView.getRoot().getChildren().add(2, new TreeItem<String>(recountApp.sysInfo.freeMemInfo()));
        settingsTreeView.getRoot().getChildren().add(3, new TreeItem<String>(recountApp.sysInfo.totalMemInfo()));
        settingsTreeView.getRoot().getChildren().add(4, new TreeItem<String>(recountApp.sysInfo.osInfo()));

        resource = (AppResource) bundleResource;
        recountApp.onUpdate(new Combat.EventHandler<CombatEvent>() {
            private ArrayList<Fight> fights;

            @Override
            public void handle(CombatEvent event) {
                popupTextLog.getItems().add(0, event.getLogdata());
                fights = recountApp.getFightList();
                updateCombatTree(fights);
                updateDamageTree(fights);
                if (!fights.isEmpty()) {
                    lastFight = fights.get(0);
                }

                settingsTreeView.getRoot().getChildren().set(0, new TreeItem<String>(recountApp.sysInfo.allocatedMemInfo()));
                settingsTreeView.getRoot().getChildren().set(1, new TreeItem<String>(recountApp.sysInfo.maxMemInfo()));
                settingsTreeView.getRoot().getChildren().set(2, new TreeItem<String>(recountApp.sysInfo.freeMemInfo()));
                settingsTreeView.getRoot().getChildren().set(3, new TreeItem<String>(recountApp.sysInfo.totalMemInfo()));
                settingsTreeView.getRoot().getChildren().set(4, new TreeItem<String>(recountApp.sysInfo.osInfo()));
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
        frame.setSize(new java.awt.Dimension(312, 265));
        frame.setLocation(new java.awt.Point(1280, 550));
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
                showFxMain();
            }
        });
    }

    private void showFxMain() {
        if (frame != null) {
            frame.dispose();
//            resource.getApp().getStage().close();
        }
        resource.getApp().getStage().close();
        resource.getApp().getStage().setOpacity(1f);
        resource.getApp().showMain();
    }

    private void runRecountApp() throws InvalidCombatlogPath {
        String combatLogPath = resource.getApp().getProps().getProperty("combatLogPath");
        if (combatLogPath == null) {
            throw new InvalidCombatlogPath();
        }
        if (!recountApp.isRunning()) {
            recountApp.run();
        }
    }

    private TreeItem<String> rootTreeView(ArrayList<Fight> fights) {
        TreeItem<String> root = new TreeItem<String>("Combat Fights");
        root.setExpanded(true);
        Iterator<Fight> iter = fights.iterator();
        int i = 0;
        while (iter.hasNext()) {
            TreeItem<String> item = createFightTree(iter.next());
            if (i == 0) {
                item.setExpanded(true);
            }
            root.getChildren().add(item);
            i++;
        }
        return root;
    }

    private TreeItem<String> createFightTree(Fight fight) {
        TreeItem<String> item = new TreeItem<String>(fight.info());

        ArrayList<Actor> fightActors = new ArrayList<Actor>(fight.getActors());
        Collections.sort(fightActors, fight.new ActorDamageDoneComparator());
        Iterator<Actor> iterActor = fightActors.iterator();

        while(iterActor.hasNext()) {
            Actor actor = iterActor.next();
            String dps = "";
            String hps = "";
            Date endTime = fight.getFinish();
            if (endTime == null) {
                endTime = new Date();
            }
            long duration = endTime.getTime() - fight.getStart().getTime();
            if (duration > 100) {
                dps = Util.valuePerSecond(actor.getDamageDone(fight), duration);
                hps = Util.valuePerSecond(actor.getHealDone(fight), duration);
                item.getChildren().add(new TreeItem<String>(actor.info(fight, dps, hps)));
            }
        }

        return item;

    }

    private void updateCombatTree(ArrayList<Fight> fights) {
        if (fights.isEmpty()) {
            return;
        }
        if (combatTreeView.getRoot() == null) {
            combatTreeView.setRoot(rootTreeView(fights));

            return;
        }
        ObservableList<TreeItem<String>> children = combatTreeView.getRoot().getChildren();

        Fight fight = fights.get(0);
        children.get(0).setExpanded(false);
        if (fight.equals(lastFight)) {
            children.set(0, createFightTree(fight));
        } else {
            children.add(0, createFightTree(fight));
        }
        //lastFight = fight;
        children.get(0).setExpanded(true);
    }

    private void updateDamageTree(ArrayList<Fight> fights) {
        if (fights.isEmpty()) {
            return;
        }
        if (combatDamageTreeView.getRoot() == null) {
            combatDamageTreeView.setRoot(rootTreeView(fights));

            return;
        }
        ObservableList<TreeItem<String>> children = combatDamageTreeView.getRoot().getChildren();

        Fight fight = fights.get(0);
        children.get(0).setExpanded(false);
        DamageFightTree treeItem = new DamageFightTree(fight);
        if (fight.equals(lastFight)) {
            children.set(0, treeItem.getItem());
        } else {
            children.add(0, treeItem.getItem());
        }
        //lastFight = fight;
        children.get(0).setExpanded(true);
    }
}
