package ir.soroushtabesh.hearthstone.views.gui.controllers;

public class NoSuchSceneException extends RuntimeException {
    public NoSuchSceneException() {
        super("Add scene before starting");
    }
}
