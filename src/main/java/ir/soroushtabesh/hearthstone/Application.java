package ir.soroushtabesh.hearthstone;

import ir.soroushtabesh.hearthstone.cli.CLIManager;
import ir.soroushtabesh.hearthstone.cli.activities.Collections;
import ir.soroushtabesh.hearthstone.cli.activities.StartPage;
import ir.soroushtabesh.hearthstone.controllers.PlayerManager;
import ir.soroushtabesh.hearthstone.util.Logger;
import ir.soroushtabesh.hearthstone.util.Seeding;

public class Application {
    public static void main(String[] args) {
        Seeding.initiate();
        Logger.log("application", "start");
        CLIManager cliManager = CLIManager.getInstance();
        cliManager.initializeProcessor();
        cliManager.startActivity(new StartPage());
        cliManager.fireUp();
        Logger.log("application", "shutdown");
        //tester();
        /*
        for deploy:
        1- enable banner
        2- disable sql log
        3- write help
        4- erase debug shit
         */
    }

    public static void tester() {
        CLIManager cliManager = CLIManager.getInstance();
        cliManager.initializeProcessor();
        PlayerManager.getInstance().authenticate("akbar", "akbar");
        cliManager.startActivity(new Collections());
        cliManager.fireUp();
    }

}
