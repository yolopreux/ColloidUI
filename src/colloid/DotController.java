package colloid;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.Set;

import colloid.App.AppResource;
import colloid.http.User;
import colloid.http.User.UnloggedUserError;
import colloid.model.event.Combat;
import colloid.model.event.CombatEvent;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

public class DotController extends AnchorPane implements Initializable {

    final RecountApp recountApp = RecountApp.getInstance();
    static AppResource resource;
    PopupWindow win;

    @FXML
    Label crushingDarkness;
    @FXML
    Label affliction;
    @FXML
    Label creepingTerror;
    @FXML
    Label wrath;

    @FXML
    Button close;
    @FXML
    GridPane gridPane;


    private final String CRUSHING_DARKNESS = "Crushing Darkness";
    private final String AFFLICTION = "Affliction";
    private final String CREEPING_TERROR = "Creeping Terror";
    private final String WRATH = "Wrath";

    private final long DURATION_CRUSHING_DARKNESS = 7000L;
    private final long DURATION_AFFLICTION = 17000L;
    private final long DURATION_CREEPING_TERROR = 17000L;
    private final long DURATION_WRATH = 30000L;

    private final String ABILITY_ACTIVATE = "AbilityActivate";
    private final String ABILITY_CANCEL = "AbilityCancel";
    private final String REMOVE_EFFECT = "RemoveEffect";
    private final String APPLY_EFFECT = "ApplyEffect";

    private final String COMBAT_EXIT = "CombatExit";

    HashSet<SpellContainer> spells = new HashSet<SpellContainer>();

    private void initAbilities() {
        crushingDarkness.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/img/crushing_darkness.jpg"))));
        crushingDarkness.setText(CRUSHING_DARKNESS);
        crushingDarkness.setVisible(false);
        spells.add(new SpellContainer(CRUSHING_DARKNESS, DURATION_CRUSHING_DARKNESS, crushingDarkness));

        affliction.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/img/affliction.jpg"))));
        affliction.setText(AFFLICTION);
        affliction.setVisible(false);
        spells.add(new SpellContainer(AFFLICTION, DURATION_AFFLICTION, affliction));

        creepingTerror.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/img/creeping_terror.jpg"))));
        creepingTerror.setText(CREEPING_TERROR);
        creepingTerror.setVisible(false);
        spells.add(new SpellContainer(CREEPING_TERROR, DURATION_CREEPING_TERROR, creepingTerror));

        wrath.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/img/wrath.jpg"))));
        wrath.setText(WRATH);
        wrath.setVisible(false);
        spells.add(new SpellContainer(WRATH, DURATION_WRATH, wrath));

    }

    @Override
    public void initialize(URL url, ResourceBundle bundleResource) {

        //gridPane.setStyle("-fx-background-color: rgba(0, 100, 100, 0.5); -fx-background-radius: 10;");

        resource = (AppResource) bundleResource;
        win = new PopupWindow(bundleResource);
        initAbilities();

        recountApp.onUpdate(new Combat.EventHandler<CombatEvent>() {
            @Override
            public void handle(CombatEvent event) {
                if ((event.getAbility() == null) || (event.getAbility().name() == null)) {
                    return;
                }
                if (event.getEffect() == null || event.getEffect().getName() == null) {
                    return;
                }
                Iterator<SpellContainer> spelIter = spells.iterator();
                while (spelIter.hasNext()) {
                    SpellContainer spell = spelIter.next();
                    if (isAbilityActivate(event, spell)) {
                        spell.runSpellTimer();
                    }
                    if (isAbilityCancel(event, spell)) {
                        spell.stopSpellTimer();
                    }
                    if (isApplyEffect(event, spell)) {
                        spell.runSpellTimer();
                    }
                    if (isRemoveEffect(event, spell)) {
                        spell.stopSpellTimer();
                    }
                    if (isCombatExit(event)) {
                        spell.stopSpellTimer();
                    }
                }
            }
        });

        gridPane.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (win == null) {
                    return;
                }
                java.awt.Point location = win.getFrame().getLocationOnScreen();
                location.setLocation(event.getScreenX(), event.getScreenY());
                win.getFrame().setLocation(location);
            }
        });
    }

    public void closeAction(ActionEvent event) {
        new InterruptedException();
        win.getFrame().dispose();
    }

    private boolean isAbilityActivate(CombatEvent event, SpellContainer spell) {
        if (event.getAbility().name().contains(spell.getName())
                && event.getEffect().getName().contains("Event")
                && event.getEffect().getName().contains(ABILITY_ACTIVATE)) {
            return true;
        }
        return false;
    }

    private boolean isAbilityCancel(CombatEvent event, SpellContainer spell) {
        if (event.getAbility().name().contains(spell.getName())
                && event.getEffect().getName().contains("Event")
                && event.getEffect().getName().contains(ABILITY_CANCEL)) {
            return true;
        }
        return false;
    }

    private boolean isApplyEffect(CombatEvent event, SpellContainer spell) {
        if (event.getEffect().getName().contains(spell.getName())
                && event.getEffect().getName().contains(APPLY_EFFECT)) {
            return true;
        }
        return false;
    }

    private boolean isRemoveEffect(CombatEvent event, SpellContainer spell) {
        if (event.getEffect().getName().contains(spell.getName())
                && event.getEffect().getName().contains(REMOVE_EFFECT)) {
            return true;
        }
        return false;
    }

    private boolean isCombatExit(CombatEvent event) {
        if (event.getLogdata().contains(COMBAT_EXIT)) {
            return true;
        }

        return false;
    }

    class SpellContainer {

        private final long duration;
        private final String name;
        private final Label label;
        private SpellTimer spellTimer = null;

        SpellContainer(String spellName, long duration, Label spellLabel) {
            this.duration = duration;
            this.name = spellName;
            this.label = spellLabel;
        }

        public void runSpellTimer() {
            App.getLogger().info(String.format("Run %s", spellTimer));
            if (spellTimer == null) {
                spellTimer = new SpellTimer(this.name, this.duration, this.label);
            }
            if (spellTimer.isRunning()) {
                spellTimer.stop();
            }
            spellTimer.run();
        }

        public void stopSpellTimer() {
            App.getLogger().info(String.format("Stop %s", spellTimer));
            if (spellTimer == null) {
                return;
            }
            spellTimer.stop();
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + getOuterType().hashCode();
            result = prime * result + ((name == null) ? 0 : name.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            SpellContainer other = (SpellContainer) obj;
            if (!getOuterType().equals(other.getOuterType()))
                return false;
            if (name == null) {
                if (other.name != null)
                    return false;
            } else if (!name.equals(other.name))
                return false;
            return true;
        }

        private DotController getOuterType() {
            return DotController.this;
        }

        public long getDuration() {
            return duration;
        }

        public String getName() {
            return name;
        }

        public Label getLabel() {
            return label;
        }

        @Override
        public String toString() {
            return String.format("SpellContainer [duration=%s, name=%s, spellTimer=%s]",
                    duration, name, spellTimer);
        }
    }

    class SpellTimer {

        private final long duration;
        private long durationleft;
        private long startTime;
        private final String name;
        private final Label label;
        String abbilityTarget = null;
        Thread thread;

        SpellTimer(String spellName, Long duration, Label spellLabel) {
            name = spellName;
            label = spellLabel;
            this.duration = duration;
        }

        String formatTime(long duration) {
            return String.format("%02d:%1d", duration/1000, (duration/100)%10);
        }

        boolean isExpired() {
            long now = new Date().getTime();
            if ((now - durationleft) > startTime) {
                return false;
            }
            return true;
        }

        public void run() {
            durationleft = duration;
            startTime = new Date().getTime();
            thread = new Thread(new Runnable() {
                @Override public void run() {
                    label.setVisible(true);
                    while (durationleft > 0) {
                        try {
                            Thread.sleep(100L);
                            durationleft = duration + startTime - new Date().getTime();
                            updateFxApp();
                        } catch (InterruptedException e) {
                            //do nothing
                            e.printStackTrace();
                        }
                    }
                    label.setVisible(false);
                }
            });
            thread.start();
        }

        public void stop() {
            if (!thread.isInterrupted()) {
                thread.interrupt();
            }
            durationleft = 0;
        }

        public boolean isRunning() {
            return (durationleft > 0);
        }

        void updateFxApp() {
            if (Platform.isFxApplicationThread()) {
                update();
            } else {
                Platform.runLater(new Runnable() {
                    @Override public void run() {
                        updateFxApp();
                    }
                });
            }
        }

        void update() {
            label.setText(formatTime(durationleft));
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + getOuterType().hashCode();
            result = prime
                    * result
                    + ((abbilityTarget == null) ? 0 : abbilityTarget.hashCode());
            result = prime * result + ((name == null) ? 0 : name.hashCode());
            result = prime * result + (int) (startTime ^ (startTime >>> 32));
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            SpellTimer other = (SpellTimer) obj;
            if (!getOuterType().equals(other.getOuterType()))
                return false;
            if (abbilityTarget == null) {
                if (other.abbilityTarget != null)
                    return false;
            } else if (!abbilityTarget.equals(other.abbilityTarget))
                return false;
            if (name == null) {
                if (other.name != null)
                    return false;
            } else if (!name.equals(other.name))
                return false;
            if (startTime != other.startTime)
                return false;
            return true;
        }

        private DotController getOuterType() {
            return DotController.this;
        }

        @Override
        public String toString() {
            return String.format("SpellTimer [duration=%s, durationleft=%s, startTime=%s, name=%s, abbilityTarget=%s]",
                    duration, durationleft, startTime, name, abbilityTarget);
        }

    }
}
