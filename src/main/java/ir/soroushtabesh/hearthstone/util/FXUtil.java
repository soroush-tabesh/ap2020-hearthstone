package ir.soroushtabesh.hearthstone.util;

import javafx.application.Platform;

public class FXUtil {
    private FXUtil() {
    }

    public static void runLater(Runnable runnable, long millis) {
        new Thread(() -> {
            try {
                Thread.sleep(millis);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Platform.runLater(runnable);
        }).start();
    }
}
