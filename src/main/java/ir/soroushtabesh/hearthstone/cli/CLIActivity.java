package ir.soroushtabesh.hearthstone.cli;

public abstract class CLIActivity {
    public abstract void onStart(String[] args);

    public abstract void onStop();

    public abstract void onReceivedCommand(String[] args);

    public abstract String getActivityCommand();
}
