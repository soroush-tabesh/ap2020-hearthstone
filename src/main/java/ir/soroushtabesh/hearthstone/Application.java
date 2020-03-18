package ir.soroushtabesh.hearthstone;

import ir.soroushtabesh.hearthstone.cli.CLIManager;
import ir.soroushtabesh.hearthstone.cli.activities.Collections;
import ir.soroushtabesh.hearthstone.cli.activities.MainMenu;
import ir.soroushtabesh.hearthstone.cli.activities.StartPage;
import ir.soroushtabesh.hearthstone.cli.activities.Store;

public class Application {
    public static void main(String[] args) {
        CLIManager cliManager = CLIManager.getInstance();
        cliManager.addActivity(new StartPage());
        cliManager.addActivity(new Collections());
        cliManager.addActivity(new MainMenu());
        cliManager.addActivity(new Store());
        cliManager.startActivity(new StartPage());
        cliManager.fireUp();
    }
}
