//package ir.soroushtabesh.hearthstone.views.cli;
//
//import ir.soroushtabesh.hearthstone.util.cli.CommandProcessor;
//
//import java.util.Scanner;
//
//public abstract class CLIActivity {
//    private CLIManager cliManager = CLIManager.getInstance();
//    private Scanner scanner = CLIManager.getInstance().getScanner();
//
//    public void onStart(String[] args) {
//
//    }
//
//    public void onStop() {
//
//    }
//
//    public void onReceivedCommand(String[] args) {
//        if (!getProcessor().process(args)) {
//            System.out.println("WTF? :/\nWell you may enter \'hearthstone --help\' if you are confused with the CLI");
//        }
//    }
//
//    public void onResume() {
//
//    }
//
//    public void onPause() {
//
//    }
//
//    public CLIManager getCLIManager() {
//        return cliManager;
//    }
//
//    public Scanner getScanner() {
//        return scanner;
//    }
//
//    public abstract String getActivityCommand();
//
//    public abstract CommandProcessor getProcessor();
//
//    public String getActivityName() {
//        return getActivityCommand();
//    }
//}
