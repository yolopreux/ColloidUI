/**
 *  Colloid project
 *
 *  Combat log analyzer.
 *
 *  copyright: (c) 2013 by Darek <netmik12 [AT] gmail [DOT] com>
 *  license: BSD, see LICENSE for more details
 */
package colloid;

import java.text.NumberFormat;

import javafx.collections.ObservableList;
import colloid.model.event.RecountLoop;

public class RecountApp  extends RecountLoop {

    ObservableList<String> textLog;
    private static RecountApp instance;
    public SysInfo sysInfo;

    protected RecountApp() {
        sysInfo = new SysInfo();
    }

    public static RecountApp getInstance() {
        if (instance == null) {
            instance = new RecountApp();
        }

        return instance;
    }

    @Override
    public void onStart() {
    }

    /**
     * Executes in Fx application thread
     */
    @Override public void onUpdate() {
    }

    @Override
    public void onStop() {
    }

    public void setTextLog(final ObservableList<String> textLog) {
        this.textLog = textLog;
    }

    public class SysInfo {

        private final Runtime runtime = Runtime.getRuntime();

        public String osName() {
            return System.getProperty("os.name");
        }

        public String osVersion() {
            return System.getProperty("os.version");
        }

        public String osArch() {
            return System.getProperty("os.arch");
        }

        public String osInfo() {
            return String.format("System: %s %s %s", osName(), osVersion(), osArch());
        }

        public long totalMem() {
            return Runtime.getRuntime().totalMemory();
        }

        public long usedMem() {
            return Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        }

        public String totalMemInfo() {
            NumberFormat format = NumberFormat.getInstance();
            long maxMemory = runtime.maxMemory();
            long allocatedMemory = runtime.totalMemory();
            long freeMemory = runtime.freeMemory();

            return String.format("Total free memory: %s",
                    format.format((freeMemory + (maxMemory - allocatedMemory)) / 1024));
        }

        public String freeMemInfo() {
            NumberFormat format = NumberFormat.getInstance();
            long freeMemory = runtime.freeMemory();

            return String.format("Free memory: %s",
                    format.format(freeMemory / 1024));
        }

        public String allocatedMemInfo() {
            NumberFormat format = NumberFormat.getInstance();
            long allocatedMemory = runtime.totalMemory();

            return String.format("Allocated memory: %s",
                    format.format(allocatedMemory / 1024));
        }

        public String maxMemInfo() {
            NumberFormat format = NumberFormat.getInstance();
            long maxMemory = runtime.maxMemory();
            return String.format("Max memory: %s",
                    format.format(maxMemory / 1024));
        }
    }
}
