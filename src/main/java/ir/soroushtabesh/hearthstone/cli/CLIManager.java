package ir.soroushtabesh.hearthstone.cli;

import ir.soroushtabesh.hearthstone.cli.activities.StartPage;
import ir.soroushtabesh.hearthstone.util.DBUtil;
import ir.soroushtabesh.hearthstone.util.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
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
    }

    public static CLIManager getInstance() {
        if (instance == null) {
            instance = new CLIManager();
        }
        return instance;
    }

    public void initializeProcessor() {
        processor.add("back", event -> {
            Logger.log("navigate", "back");
            if (!stopCurrent()) {
                shutdown();
            }
        });
        processor.add("hearthstone", event -> {
            if (event.args.length > 0 && event.args[0].equals("--help")) {
                showHelp();
                Logger.log("help", "");
            } else {
                System.out.println("Did you mean 'hearthstone --help'?");
            }
        });
        addActivity(new StartPage());
    }

    private void showHelp() {
        System.out.println(">>help");
        System.out.println(">>navigational and global commands");
        for (String s : processor.getCommandList()) {
            System.out.println(s);
        }
        System.out.println(">>commands in context of '" + getCurrentActivity().getActivityName() + "'");
        for (String s : getCurrentActivity().getProcessor().getCommandList()) {
            System.out.println(s);
        }
        System.out.println(">>universal help");
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        File file = new File(classLoader.getResource("help.txt").getFile());
        String content = "null";
        try {
            content = new String(Files.readAllBytes(file.toPath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(content);
    }

    public void clearHistory() {
        currentActivity.subList(0, currentActivity.size() - 1).clear();
        processor.clearCommandList();
        initializeProcessor();
    }

    public CLIActivity getCurrentActivity() {
        return currentActivity.get(currentActivity.size() - 1);
    }

    public Scanner getScanner() {
        return scanner;
    }

    public void addActivity(CLIActivity activity) {
        processor.add(activity.getActivityCommand(), event -> {
            if (getCurrentActivity().getClass().equals(activity.getClass()))
                currentActivity.remove(currentActivity.size() - 1);
            startActivity(activity, event.args);
        });
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
        Logger.log("navigate", activity.getActivityName());
    }

    public void fireUp() {
        Logger.log("CLI", "fireUp");
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
        DBUtil.shutdown();
        Logger.log("CLI", "shutdown");
    }

    public boolean stopCurrent() {
        CLIActivity temp = currentActivity.remove(currentActivity.size() - 1);
        temp.onPause();
        temp.onStop();
        if (!currentActivity.isEmpty())
            currentActivity.get(currentActivity.size() - 1).onResume();
        return !currentActivity.isEmpty();
    }
}
