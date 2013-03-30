package colloid.http;

public class Util {

    public static final void goToSleep(long Duration) {
        long Delay = System.currentTimeMillis() + Duration;
        while (System.currentTimeMillis()<Delay) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException Ex) {
                // We don't care
            }
        }
    }
}
