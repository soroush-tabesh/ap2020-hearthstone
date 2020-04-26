package ir.soroushtabesh.hearthstone;

import ir.soroushtabesh.hearthstone.cli.CLIManager;
import ir.soroushtabesh.hearthstone.cli.activities.Collections;
import ir.soroushtabesh.hearthstone.cli.activities.StartPage;
import ir.soroushtabesh.hearthstone.controllers.PlayerManager;

public class ApplicationCLI {

    public static void start(String[] args) {
        CLIManager cliManager = CLIManager.getInstance();
        cliManager.initializeProcessor();
        cliManager.startActivity(new StartPage());
        cliManager.fireUp();
    }

    public static void tester() {
        CLIManager cliManager = CLIManager.getInstance();
        cliManager.initializeProcessor();
        PlayerManager.getInstance().authenticate("akbar", "akbar");
        cliManager.startActivity(new Collections());
        cliManager.fireUp();
    }

}
