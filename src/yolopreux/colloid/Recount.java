package yolopreux.colloid;

import java.io.File;

public class Recount {

    private String path;
    protected File[] logs;
    private static Recount instance;
    private boolean isRunning;

    protected Recount() {
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
                    System.out.println(isRunning);
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
            System.out.print(LogUtil.tail(log));
        }
    }
}
