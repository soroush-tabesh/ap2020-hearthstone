package ir.soroushtabesh.hearthstone.util;

import javafx.application.Platform;
import javafx.scene.control.Alert;

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

    public static void showAlertInfo(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
