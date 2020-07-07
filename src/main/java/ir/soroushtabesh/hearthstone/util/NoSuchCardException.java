package ir.soroushtabesh.hearthstone.util;

public class NoSuchCardException extends RuntimeException {
    public NoSuchCardException(String message) {
        super(message);
    }
}
