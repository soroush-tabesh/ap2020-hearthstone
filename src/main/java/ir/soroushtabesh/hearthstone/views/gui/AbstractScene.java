package ir.soroushtabesh.hearthstone.views.gui;

import animatefx.animation.AnimateFXInterpolator;
import ir.soroushtabesh.hearthstone.views.gui.controllers.AbstractSceneController;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public abstract class AbstractScene {
    private Pane pane;
    private AbstractSceneController controller;

    public AbstractScene() {
        String format = String.format("fxml/%s.fxml", getClass().getSimpleName());
        FXMLLoader fxmlLoader = new FXMLLoader(getClass()
                .getResource(format));
        Pane root = null;
        try {
            root = fxmlLoader.load();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(format);
            return;
        }
        controller = fxmlLoader.getController();
        setPane(root);
    }

    public Pane getPane() {
        return pane;
    }

    private void setPane(Pane pane) {
        this.pane = pane;
    }

    public AbstractSceneController getController() {
        return controller;
    }

    public void fadeIn() {
        getController().setDisable(false);
    }

    public void fadeOut() {
        //todo: use fade and scale
        //todo: disable after fade
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.millis(0)
                        , new KeyValue(getPane().opacityProperty(), 1, AnimateFXInterpolator.EASE)
                        , new KeyValue(getPane().scaleXProperty(), 1, AnimateFXInterpolator.EASE)
                        , new KeyValue(getPane().scaleYProperty(), 1, AnimateFXInterpolator.EASE)
                ),
                new KeyFrame(Duration.millis(500)
                        , new KeyValue(getPane().opacityProperty(), 0, AnimateFXInterpolator.EASE)
                        , new KeyValue(getPane().scaleXProperty(), 2, AnimateFXInterpolator.EASE)
                        , new KeyValue(getPane().scaleYProperty(), 2, AnimateFXInterpolator.EASE)
                ),
                new KeyFrame(Duration.millis(1000),
                        new KeyValue(getPane().opacityProperty(), 0, AnimateFXInterpolator.EASE)
                )
        );
        timeline.setOnFinished((evt) -> {
            getController().setDisable(true);
            getPane().setOpacity(1);
            getPane().setScaleX(1);
            getPane().setScaleY(1);
        });
        timeline.play();
    }

}
