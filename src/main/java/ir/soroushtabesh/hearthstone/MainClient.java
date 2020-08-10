package ir.soroushtabesh.hearthstone;

import ir.soroushtabesh.hearthstone.util.Logger;

public class MainClient {
    public static void main(String[] args) {
        Logger.log("application", "start");
        ApplicationGUI.start(args);
        Logger.log("application", "shutdown");
    }
}
