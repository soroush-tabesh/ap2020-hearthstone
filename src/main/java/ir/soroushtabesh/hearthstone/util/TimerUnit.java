package ir.soroushtabesh.hearthstone.util;

import ir.soroushtabesh.hearthstone.util.gui.FXUtil;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.concurrent.atomic.AtomicInteger;

public class TimerUnit {
    private final IntegerProperty remTime = new SimpleIntegerProperty();
    private Runnable onTimerEnd;
    private Runnable onTimerPreEnd;
    private Thread thread;
    private final AtomicInteger counter = new AtomicInteger(0);

    public void startTimer(int value, int preEnd) {
        if (thread != null)
            thread.interrupt();
        counter.set(value);
        thread = new Thread(() -> {
            boolean flag = true;
            while (flag) {
                FXUtil.runLater(() -> remTime.set(counter.decrementAndGet()), 0);
                if (counter.get() == preEnd && onTimerPreEnd != null)
                    FXUtil.runLater(onTimerPreEnd, 0);
                if (counter.get() == 0) {
                    if (onTimerEnd != null)
                        FXUtil.runLater(onTimerEnd, 0);
                    flag = false;
                    break;
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    break;
                }
            }
        });
        thread.start();
    }

    public void stop() {
        thread.interrupt();
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
