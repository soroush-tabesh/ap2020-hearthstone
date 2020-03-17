package ir.soroushtabesh.hearthstone;

import ir.soroushtabesh.hearthstone.cli.CLIManager;
import ir.soroushtabesh.hearthstone.cli.activities.StartPage;

public class Application {
    public static void main(String[] args) {
        CLIManager cliManager = CLIManager.getInstance();
        cliManager.addActivity(new StartPage());
        //todo: add others
        cliManager.startActivity(new StartPage());
    }
}
