package ir.soroushtabesh.hearthstone.util.cli;

public class ProcessListenerEvent {
    public String cmd;
    public String[] args;

    public ProcessListenerEvent(String cmd, String[] args) {
        this.cmd = cmd;
        this.args = args;
    }
}
