package ir.soroushtabesh.hearthstone.util;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class TimerUnit {
    private IntegerProperty remTime = new SimpleIntegerProperty();
    private Runnable onTimerEnd;
    private Runnable onTimerPreEnd;
    private Thread thread;
    private int counter;

    public void startTimer(int value, int preEnd) {
        if (thread != null)
            thread.interrupt();
        counter = value;
        thread = new Thread(() -> {
            boolean flag = true;
            while (flag) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                }
                FXUtil.runLater(() -> remTime.set(--counter), 0);
                if (counter == preEnd && onTimerPreEnd != null)
                    FXUtil.runLater(onTimerPreEnd, 0);
                if (counter == 0) {
                    if (onTimerEnd != null)
                        FXUtil.runLater(onTimerEnd, 0);
                    flag = false;
                }
            }
        });
        thread.start();
    }

    public int getRemTime() {
        return remTime.get();
    }

    public ReadOnlyIntegerProperty remTimeProperty() {
        return remTime;
    }

    public void setOnTimerEnd(Runnable runnable) {
        onTimerEnd = runnable;
    }

    public void setOnTimerPreEnd(Runnable onTimerPreEnd) {
        this.onTimerPreEnd = onTimerPreEnd;
    }
}
