package ir.soroushtabesh.hearthstone.util;

import animatefx.animation.AnimateFXInterpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.util.Duration;

public class AnimationUtil {
    private AnimationUtil() {
    }

    public static Timeline getSceneFadeOut(Node node) {
        return new Timeline(
                new KeyFrame(Duration.millis(0)
                        , new KeyValue(node.opacityProperty(), 1, AnimateFXInterpolator.EASE)
                        , new KeyValue(node.scaleXProperty(), 1, AnimateFXInterpolator.EASE)
                        , new KeyValue(node.scaleYProperty(), 1, AnimateFXInterpolator.EASE)
                ),
                new KeyFrame(Duration.millis(500)
                        , new KeyValue(node.opacityProperty(), 0, AnimateFXInterpolator.EASE)
                        , new KeyValue(node.scaleXProperty(), 2, AnimateFXInterpolator.EASE)
                        , new KeyValue(node.scaleYProperty(), 2, AnimateFXInterpolator.EASE)
                ),
                new KeyFrame(Duration.millis(1000),
                        new KeyValue(node.opacityProperty(), 0, AnimateFXInterpolator.EASE)
                )
        );
    }

    public static Timeline getSceneFadeIn(Node node) {
        return new Timeline(
                new KeyFrame(Duration.millis(0),
                        new KeyValue(node.opacityProperty(), 0, AnimateFXInterpolator.EASE)
                ),
                new KeyFrame(Duration.millis(500),
                        new KeyValue(node.opacityProperty(), 1, AnimateFXInterpolator.EASE)
                )

        );
    }
}
