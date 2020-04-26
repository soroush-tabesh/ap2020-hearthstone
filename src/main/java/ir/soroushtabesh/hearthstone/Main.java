package ir.soroushtabesh.hearthstone;

import ir.soroushtabesh.hearthstone.util.Logger;
import ir.soroushtabesh.hearthstone.util.Seeding;

public class Main {
    public static void main(String[] args) {
        Seeding.initiate();
        Logger.log("application", "start");
        ApplicationGUI.start(args);
        Logger.log("application", "shutdown");
    }
}
