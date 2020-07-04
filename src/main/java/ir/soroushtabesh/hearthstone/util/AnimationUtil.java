package ir.soroushtabesh.hearthstone.util;

import animatefx.animation.AnimateFXInterpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
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

    public static Effect getGlowAnimated(Color color, double minDepth, double maxDepth) {
        DropShadow borderGlow = new DropShadow();
        borderGlow.setColor(color);
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.millis(0),
                        new KeyValue(borderGlow.heightProperty(), minDepth, AnimateFXInterpolator.EASE),
                        new KeyValue(borderGlow.widthProperty(), minDepth, AnimateFXInterpolator.EASE)
                ),
                new KeyFrame(Duration.millis(1000),
                        new KeyValue(borderGlow.heightProperty(), maxDepth, AnimateFXInterpolator.EASE),
                        new KeyValue(borderGlow.widthProperty(), maxDepth, AnimateFXInterpolator.EASE)
                )
        );
        timeline.setAutoReverse(true);
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
        return borderGlow;
    }

    public static Timeline getPulse(Node node) {
        return new Timeline(
                new KeyFrame(Duration.millis(0),
                        new KeyValue(node.scaleXProperty(), 1, AnimateFXInterpolator.EASE),
                        new KeyValue(node.scaleYProperty(), 1, AnimateFXInterpolator.EASE),
                        new KeyValue(node.scaleZProperty(), 1, AnimateFXInterpolator.EASE)
                ),
                new KeyFrame(Duration.millis(300),
                        new KeyValue(node.scaleXProperty(), 2, AnimateFXInterpolator.EASE),
                        new KeyValue(node.scaleYProperty(), 2, AnimateFXInterpolator.EASE),
                        new KeyValue(node.scaleZProperty(), 2, AnimateFXInterpolator.EASE)
                ),
                new KeyFrame(Duration.millis(600),
                        new KeyValue(node.scaleXProperty(), 1, AnimateFXInterpolator.EASE),
                        new KeyValue(node.scaleYProperty(), 1, AnimateFXInterpolator.EASE),
                        new KeyValue(node.scaleZProperty(), 1, AnimateFXInterpolator.EASE)
                )
        );
    }

    public static Timeline getTada(Node node) {
        node.setRotationAxis(Rotate.Z_AXIS);
        return new Timeline(
                new KeyFrame(Duration.millis(0),
                        new KeyValue(node.rotateProperty(), 0, AnimateFXInterpolator.EASE)

                ),
                new KeyFrame(Duration.millis(100),
                        new KeyValue(node.rotateProperty(), -3, AnimateFXInterpolator.EASE)
                ),
                new KeyFrame(Duration.millis(200),
                        new KeyValue(node.rotateProperty(), -3, AnimateFXInterpolator.EASE)
                ),
                new KeyFrame(Duration.millis(300),
                        new KeyValue(node.rotateProperty(), 3, AnimateFXInterpolator.EASE)
                ),
                new KeyFrame(Duration.millis(400),
                        new KeyValue(node.rotateProperty(), -3, AnimateFXInterpolator.EASE)
                ),
                new KeyFrame(Duration.millis(500),
                        new KeyValue(node.rotateProperty(), 3, AnimateFXInterpolator.EASE)
                ),
                new KeyFrame(Duration.millis(600),
                        new KeyValue(node.rotateProperty(), -3, AnimateFXInterpolator.EASE)
                ),
                new KeyFrame(Duration.millis(700),
                        new KeyValue(node.rotateProperty(), 3, AnimateFXInterpolator.EASE)
                ),
                new KeyFrame(Duration.millis(800),
                        new KeyValue(node.rotateProperty(), -3, AnimateFXInterpolator.EASE)
                ),
                new KeyFrame(Duration.millis(900),
                        new KeyValue(node.rotateProperty(), 3, AnimateFXInterpolator.EASE)
                ),
                new KeyFrame(Duration.millis(1000),
                        new KeyValue(node.rotateProperty(), 0, AnimateFXInterpolator.EASE)
                )
        );
    }
}
