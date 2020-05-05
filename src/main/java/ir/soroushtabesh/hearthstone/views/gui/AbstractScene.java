package ir.soroushtabesh.hearthstone.views.gui;

import ir.soroushtabesh.hearthstone.util.AnimationUtil;
import ir.soroushtabesh.hearthstone.views.gui.controllers.AbstractSceneController;
import javafx.animation.Timeline;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

public abstract class AbstractScene {
    private Pane pane;
    private AbstractSceneController controller;

    public AbstractScene() {
        String format = String.format("fxml/%s.fxml", getClass().getSimpleName());
        FXMLLoader fxmlLoader = new FXMLLoader(getClass()
                .getResource(format));
        Pane root;
        try {
            root = fxmlLoader.load();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(format);
            return;
        }
        setController(fxmlLoader.getController());
        getController().setCurrentScene(this);
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

    private void setController(AbstractSceneController controller) {
        this.controller = controller;
    }

    public void fadeIn() {
        getController().setDisable(false);
        getPane().setScaleX(1);
        getPane().setScaleY(1);
        Timeline timeline = AnimationUtil.getSceneFadeIn(getPane());
        timeline.setOnFinished((evt) -> getPane().setOpacity(1));
        timeline.play();
    }

    public void fadeOut() {
        Timeline timeline = AnimationUtil.getSceneFadeOut(getPane());
        timeline.setOnFinished((evt) -> {
            getController().setDisable(true);
            getPane().setOpacity(1);
            getPane().setScaleX(1);
            getPane().setScaleY(1);
        });
        timeline.play();
    }

    public void onStart(Object message) {
        getController().onStart(message);
    }

    public void onStop() {
        getController().onStop();
    }


}
