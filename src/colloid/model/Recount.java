package colloid.model;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.scene.control.TextArea;

public class Recount {

    private String path;
    protected File[] logs;
    private static Recount instance;
    private boolean isRunning;
    String lastLine = null;
    ArrayList<String> data = new ArrayList<String>();
    TextArea textLog;
    TextArea recountLog;
    Combat combat;

    protected Recount() {
        combat = new Combat();
    }

    public static Recount getInstance() {
        if (instance == null) {
            instance = new Recount();
        }

        return instance;
    }

    public void run() {
        isRunning = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (isRunning) {
                    parse();
                }
            }
        }).start();
    }

    public void stop() {
        isRunning = false;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public Recount setCombatDirPath(String dirPath) {
        init(dirPath);
        return this;
    }

    private void init(String path) {
        this.path = path;
        File dir = new File(path);
        logs = dir.listFiles();
    }

    public void parse() {
        if (logs == null || logs.length == 0) {
            return;
        }
        File log = logs[logs.length - 1];
        /**
         * @TODO
         */
        if (log.isFile()) {
            String tailLines = LogUtil.tail(log, 10);
            String[] lines = tailLines.split("\n");
            for (String line : lines) {
                Date logTime = parseDate(line);
                if (logTime == null) {
                    continue;
                }
                if (lastLine == null || !isLogged(logTime)) {
                    lastLine = line;
                    countCombat(line);
                    if (textLog != null && line != null) {
                        synchronized (textLog) {
                            textLog.insertText(0, line + "\n");
                        }
                    }
                }
            }
        }
    }

    private Date parseDate(String line) {
        final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss.S",
                Locale.getDefault());
        Date logTime;
        try {
            logTime = timeFormat.parse(line.substring(1, 13));
            return logTime;
        } catch (ParseException e) {
            System.out.println(e.getMessage());
        } catch (StringIndexOutOfBoundsException e) {
            System.out.println(e);
        }

        return null;
    }

    private boolean isLogged(Date logtime) {
        if (logtime.getTime() > parseDate(lastLine).getTime()) {
            return false;
        }

        return true;
    }

    public ArrayList<String> getData() {
        return data;
    }

    public String getLastLine() {
        return lastLine;
    }

    public void setTextLog(final TextArea textLog) {
        this.textLog = textLog;
    }

    public void setRecountLog(final TextArea recountLog) {
        this.recountLog = recountLog;
    }

    protected void countCombat(String event) {
        combat.add(event).recount();
    }

    abstract class CombatEntity {

        String logData;
        String name;
        String type;
        float threat;
        float value;

        public CombatEntity(String log) {

            logData = log;
            parse();
        }
        abstract void parse();

        @Override
        public String toString() {
            return "CombatEntity [logData=" + logData + ", name=" + name
                    + ", type=" + type + ", threat=" + threat + ", value="
                    + value + "]";
        }
    }

    abstract class EffectCombatEntity extends CombatEntity {

        public EffectCombatEntity(String log) {
            super(log);
        }
        abstract boolean isEnterCombat();
        abstract boolean isExitCombat();
        abstract boolean isDamage();
        abstract boolean isHeal();

        double getValue() {
            double result = 0;
            Matcher matcher = Pattern.compile("\\((\\d+)(.*)\\)").matcher(logData);
            if (matcher.find()) {
                try {
                    result = Double.parseDouble(matcher.group(1));
                } catch (NumberFormatException ex) {
                    result = 0;
                }
            }

            return result;
        }
    }

    class CombatEvent {

        CombatEntity actor;
        CombatEntity target;
        CombatEntity ability;
        EffectCombatEntity effect;

        CombatEvent(final String[] data) {
            try {
                actor = new CombatEntity(data[1]) {
                    @Override
                    void parse() {
                        name = logData;
                    }
                };
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println(e);
            }
            try {
                target = new CombatEntity(data[2]) {
                    @Override
                    void parse() {
                        name = logData;
                    }
                };
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println(e);
            }
            try {
                ability = new CombatEntity(data[3]) {
                    @Override
                    void parse() {
                    }
                };
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println(e);
            }
            try {
                effect = new EffectCombatEntity(data[4]) {
                    @Override
                    void parse() {
                    }
    
                    @Override
                    boolean isEnterCombat() {
                        return logData.contains("EnterCombat");
                    }
    
                    @Override
                    boolean isExitCombat() {
                        return logData.contains("ExitCombat");
                    }
    
                    @Override
                    boolean isDamage() {
                        return logData.contains("Damage");
                    }
    
                    @Override
                    boolean isHeal() {
                        return logData.contains("Heal");
                    }
                };
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println(e);
            }
        }

        @Override
        public String toString() {
            return "CombatEvent [actor=" + actor + ", target=" + target
                    + ", ability=" + ability + ", effect=" + effect + "]";
        }
    }

    class Combat {

        CombatEvent event;

        HashMap<String, HashMap<String, Double>> data;

        Combat() {
            data = new HashMap<String, HashMap<String, Double>>();
        }

        Combat add(String newEvent) {
            String[] items = newEvent.substring(1).split("\\]\\s\\[");
            if (items != null) {
                event = new CombatEvent(items);
            }

            return this;
        }

        Combat recount() {
            if (event.effect == null) {
                return this;
            }
            if (event.effect.isEnterCombat()) {
                if (!data.containsKey(event.actor.name)) {
                    data.put(event.actor.name, new HashMap<String, Double>());
                }
            }
            if (event.effect.isDamage()) {
                if (!data.containsKey(event.actor.name)) {
                    data.put(event.actor.name, new HashMap<String, Double>());
                }
                if (!data.get(event.actor.name).containsKey("damage")) {
                    data.get(event.actor.name).put("damage",
                            event.effect.getValue());
                } else {
                    double damage = data.get(event.actor.name).get("damage")
                            + event.effect.getValue();
                    data.get(event.actor.name).put("damage", damage);
                }
            }

            synchronized (recountLog) {
                String text = "Damage:" + "\n";
                for (String key: data.keySet()) {
                    text = text + "Actor: " + key + "\n";
                    if (data.get(key).containsKey("damage")) {
                        text = text + "Damage: " + Double.toString(data.get(key).get("damage")) + "\n"; 
                    }
                }
                recountLog.setText(text);
            }
            return this;
        }
    }
}
