package ir.soroushtabesh.hearthstone.cli.activities;

import ir.soroushtabesh.hearthstone.cli.CLIActivity;
import ir.soroushtabesh.hearthstone.cli.CommandProcessor;
import ir.soroushtabesh.hearthstone.controllers.PlayerManager;
import ir.soroushtabesh.hearthstone.util.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class StartPage extends CLIActivity {

    private CommandProcessor processor = new CommandProcessor();
    private boolean dummy = false;

    private void signUp() {
        System.out.println("Let's make a new account!");
        System.out.println("Tell me about yourself :)");
        System.out.println("Username:");
        String username = getScanner().nextLine();
        System.out.println("Password:");
        String password = getScanner().nextLine();
        PlayerManager.Message msg = PlayerManager.getInstance().makeAccount(username, password);
        switch (msg) {
            case ERROR:
                System.out.println("An unknown error has occurred. Check Logs.");
                break;
            case EXISTS:
                System.out.println("This username is taken :(");
                break;
            case SUCCESS:
                System.out.println("Successfully created!\nNow you may login.");
        }
    }

    private void logIn() {
        System.out.println("Ok; So who are you? :)");
        System.out.println("Username:");
        String username = getScanner().nextLine();
        System.out.println("Password:");
        String password = getScanner().nextLine();
        PlayerManager.Message msg = PlayerManager.getInstance().authenticate(username, password);
        switch (msg) {
            case SUCCESS:
                System.out.println("Logged in!");
                getCLIManager().startActivity(new MainMenu());
                break;
            case ERROR:
                System.out.println("An unknown error has occurred. Check Logs.");
                break;
            case WRONG:
                System.out.println("Invalid Username or Password...");
        }
    }

    @Override
    public void onStart(String[] args) {
        getCLIManager().clearHistory();
        if (args.length > 0 && args[0].equals("-a")) {
            getCLIManager().stopCurrent();
            dummy = true;
        } else {
            ClassLoader classLoader = ClassLoader.getSystemClassLoader();
            File file = new File(classLoader.getResource("banner.txt").getFile());
            String content = "null";
            try {
                content = new String(Files.readAllBytes(file.toPath()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(content);
        }
    }

    @Override
    public void onResume() {
        if (dummy)
            return;
        PlayerManager manager = PlayerManager.getInstance();
        if (manager.logout()) {
            System.out.println("Logged out.");
        }
        System.out.println("::Start Page");
        System.out.println("Hey there! Welcome.");
        System.out.println("Do you already have an account?(login/sign-up)");
        processor.add("sign-up", event -> signUp());
        processor.add("login", event -> logIn());
    }

    @Override
    public void onStop() {
        System.out.println("Bye bye! Have a nice day buddy!");
        System.out.println("P.S. HearthStone is a real bullshit :)");
        Logger.log("exit", "");
    }

    @Override
    public String getActivityCommand() {
        return "exit";
    }

    @Override
    public CommandProcessor getProcessor() {
        return processor;
    }

    @Override
    public String getActivityName() {
        return "start page";
    }
}
