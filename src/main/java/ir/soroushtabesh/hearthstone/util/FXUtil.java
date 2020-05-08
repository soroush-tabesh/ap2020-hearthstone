package ir.soroushtabesh.hearthstone.util;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;

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

    public static Optional<ButtonType> showAlert(String title, String header, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        return alert.showAndWait();
    }

    public static void showAlertInfo(String title, String header, String content) {
        showAlert(title, header, content, Alert.AlertType.INFORMATION);
    }

    public static <T extends Parent> void loadFXMLasRoot(T component) {
        FXMLLoader loader = new FXMLLoader();
        loader.setRoot(component);
        loader.setControllerFactory(theClass -> component);
        String fileName = component.getClass().getSimpleName() + ".fxml";
        try {
            loader.load(component.getClass().getResourceAsStream(fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static URL getFXMLResource(Class<?> cls) {
        String format = String.format("fxml/%s.fxml", cls.getSimpleName());
        return cls.getResource(format);
    }
}
