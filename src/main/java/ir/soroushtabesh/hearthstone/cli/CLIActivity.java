package ir.soroushtabesh.hearthstone.cli;

public abstract class CLIActivity {
    public void onStart(String[] args) {

    }

    public void onStop() {

    }

    public void onReceivedCommand(String[] args) {

    }

    public void onResume() {

    }

    public void onPause() {

    }

    public abstract String getActivityCommand();
}
