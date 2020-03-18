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
            if (!stopCurrent()) {
                shutdown();
            }
        });
        processor.add("hearthstone", event -> {
            if (event.args.length > 0 && event.args[0].equals("--help")) {
                showHelp();
            }
        });
    }

    private void showHelp() {
        //todo
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
        if (!currentActivity.isEmpty())
            currentActivity.get(currentActivity.size() - 1).onPause();
        currentActivity.add(activity);
        activity.onStart(args);
        activity.onResume();
    }

    public void fireUp() {
        fired = true;
        while (!currentActivity.isEmpty() && fired && scanner.hasNextLine()) {
            String[] line = scanner.nextLine().split(" ");
            if (!processor.process(line)) {
                getCurrentActivity().onReceivedCommand(line);
            }
        }
        fired = false;
    }

    public void shutdown() {
        fired = false;
    }

    public boolean stopCurrent() {
        CLIActivity temp = currentActivity.remove(currentActivity.size() - 1);
        temp.onPause();
        temp.onStop();
        currentActivity.get(currentActivity.size() - 1).onResume();
        return !currentActivity.isEmpty();
    }
}
