package ir.soroushtabesh.hearthstone;

import ir.soroushtabesh.hearthstone.cli.CLIManager;
import ir.soroushtabesh.hearthstone.cli.activities.StartPage;

public class Application {
    public static void main(String[] args) {
        CLIManager cliManager = CLIManager.getInstance();
        cliManager.startActivity(new StartPage());
        cliManager.fireUp();
        /*
        for deploy:
        1- enable banner
        2- disable sql log
        3- write help
         */
    }
}
