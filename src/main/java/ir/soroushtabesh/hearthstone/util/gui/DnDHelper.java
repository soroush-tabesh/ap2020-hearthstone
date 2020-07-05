package ir.soroushtabesh.hearthstone.util.gui;

import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.paint.Color;
import javafx.scene.transform.Scale;

import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.function.Function;

public class DnDHelper {
    private final AnimationPool animationPool;

    public DnDHelper(AnimationPool animationPool) {
        this.animationPool = animationPool;
    }

    public void addDropDetector(Node node, Function<DragEvent, Boolean> function, TransferMode... transferModes) {
        node.setOnDragOver(event -> {
            event.acceptTransferModes(transferModes);
            if (Arrays.asList(transferModes).contains(event.getAcceptedTransferMode()))
                event.consume();
        });
        addDragHoverEffect(node, transferModes);
        node.setOnDragDropped(event -> {
            if (!Arrays.asList(transferModes).contains(event.getAcceptedTransferMode()))
                return;
            if (!function.apply(event))
                return;
            event.setDropCompleted(true);
            event.consume();
        });
    }

    public void addCardDragDetector(Node node, int objectId, Callable<TransferMode> callable) {
        node.setOnDragDetected(event -> {
            TransferMode transferMode;
            try {
                transferMode = callable.call();
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
            if (transferMode == null)
                return;

            SnapshotParameters sp = new SnapshotParameters();
            sp.setFill(Color.TRANSPARENT);
            sp.setTransform(new Scale(0.3, 0.3));

            Dragboard dragboard = node.startDragAndDrop(transferMode);

            ClipboardContent content = new ClipboardContent();
            content.putImage(node.snapshot(sp, null));
            content.putString(objectId + "");

            dragboard.setContent(content);
            event.consume();
        });
    }

    public void addDragHoverEffect(Node node, TransferMode... transferModes) {
        node.setOnDragEntered(event -> applyDragEnterEffect((Node) event.getSource(), event, transferModes));
        node.setOnDragExited(event -> applyDragExitEffect((Node) event.getSource(), event));
    }

    private void applyDragEnterEffect(Node node, DragEvent event, TransferMode... transferModes) {
        event.acceptTransferModes(transferModes);
        if (Arrays.asList(transferModes).contains(event.getAcceptedTransferMode()))
            animationPool.startAnimation(node, AnimationUtil.getDragHover(node, 1.1));
    }

    private void applyDragExitEffect(Node node, DragEvent event) {
        animationPool.stopAnimation(node);
    }
}
