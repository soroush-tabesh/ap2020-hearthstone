package ir.soroushtabesh.hearthstone.cli;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CLIManager {

    private static CLIManager instance;
    private boolean fired = false;
    private List<CLIActivity> currentActivity = new ArrayList<>();
    private Scanner scanner = new Scanner(System.in);
    private CommandProcessor processor = new CommandProcessor();


    private CLIManager() {
        processor.add("back", event -> {
            if (!stopCurrent()) System.err.println("Error:: Nowhere to go back...");
        });
    }

    public static CLIManager getInstance() {
        if (instance == null) {
            instance = new CLIManager();
        }
        return instance;
    }

    public void clearHistory() {
        currentActivity.subList(0, currentActivity.size() - 1).clear();
    }

    public CLIActivity getCurrentActivity() {
        return currentActivity.get(currentActivity.size() - 1);
    }

    public Scanner getScanner() {
        return scanner;
    }

    public void addActivity(CLIActivity activity) {
        processor.add(activity.getActivityCommand(), event -> startActivity(activity, event.args));
    }

    public void startActivity(CLIActivity activity) {
        startActivity(activity, new String[]{});
    }

    public void startActivity(CLIActivity activity, String[] args) {
        currentActivity.add(activity);
        activity.onStart(args);
        if (!fired)
            fire();
    }

    public void fire() {
        fired = true;
        while (fired && scanner.hasNextLine()) {
            String[] line = scanner.nextLine().split(" ");
            if (!processor.process(line)) {
                getCurrentActivity().onReceivedCommand(line);
            }
        }
    }

    public void shutdown() {
        fired = false;
    }

    public boolean stopCurrent() {
        if (currentActivity.size() <= 1)
            return false;
        currentActivity.remove(currentActivity.size() - 1).onStop();
        return true;
    }
}
