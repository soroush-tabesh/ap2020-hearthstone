//package ir.soroushtabesh.hearthstone;
//
//import ir.soroushtabesh.hearthstone.controllers.PlayerManager;
//import ir.soroushtabesh.hearthstone.views.cli.CLIManager;
//import ir.soroushtabesh.hearthstone.views.cli.activities.Collections;
//import ir.soroushtabesh.hearthstone.views.cli.activities.StartPage;
//
///**
// * <b>**Deprecated**</b><br>
// * Data models have been drastically changed in phase 2 and
// * {@link ApplicationCLI} haven't been updated since.<br>
// * Hence using it will definitely result in crash.
// */
//@Deprecated
//public class ApplicationCLI {
//
//    public static void start(String[] args) {
//        CLIManager cliManager = CLIManager.getInstance();
//        cliManager.initializeProcessor();
//        cliManager.startActivity(new StartPage());
//        cliManager.fireUp();
//    }
//
//    public static void tester() {
//        CLIManager cliManager = CLIManager.getInstance();
//        cliManager.initializeProcessor();
//        PlayerManager.getInstance().authenticate("akbar", "akbar");
//        cliManager.startActivity(new Collections());
//        cliManager.fireUp();
//    }
//
//}
