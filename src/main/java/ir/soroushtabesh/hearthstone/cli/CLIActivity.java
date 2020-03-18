package ir.soroushtabesh.hearthstone.cli;

import java.util.Scanner;

public abstract class CLIActivity {
    private CLIManager cliManager = CLIManager.getInstance();
    private Scanner scanner = CLIManager.getInstance().getScanner();

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

    public CLIManager getCLIManager() {
        return cliManager;
    }

    public Scanner getScanner() {
        return scanner;
    }

    public abstract String getActivityCommand();
}
