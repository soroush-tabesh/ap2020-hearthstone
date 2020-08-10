//package ir.soroushtabesh.hearthstone.views.cli.activities;
//
//import ir.soroushtabesh.hearthstone.controllers.PlayerManager;
//import ir.soroushtabesh.hearthstone.util.Logger;
//import ir.soroushtabesh.hearthstone.util.cli.CommandProcessor;
//import ir.soroushtabesh.hearthstone.views.cli.CLIActivity;
//import ir.soroushtabesh.hearthstone.views.cli.CLIManager;
//
//public class MainMenu extends CLIActivity {
//
//    private CommandProcessor processor = new CommandProcessor();
//
//    @Override
//    public void onStart(String[] args) {
//        CLIManager cliManager = getCLIManager();
//        cliManager.addActivity(new StartPage());
//        cliManager.addActivity(new Collections());
//        cliManager.addActivity(new MainMenu());
//        cliManager.addActivity(new Store());
//        System.out.println("::Main Menu");
//        System.out.println("collections");
//        System.out.println("store");
//        System.out.println("exit");
//        System.out.println("back");
//        System.out.println("hearthstone");
//        System.out.println("delete-user");
//        processor.add("delete-user", event -> {
//            Logger.log("delete-user", "attempt");
//            System.out.println("Enter your password:");
//            String password = CLIManager.getInstance().getScanner().nextLine();
//            PlayerManager.Message msg = PlayerManager.getInstance().deleteAccount(password);
//            switch (msg) {
//                case WRONG:
//                    System.out.println("You don't even know your own password? try again.");
//                    break;
//                case ERROR:
//                    System.out.println("An unknown error has occurred. Check Logs.");
//                    break;
//                case SUCCESS:
//                    System.out.println("Yeah... your identity is now gone...");
//                    CLIManager.getInstance().startActivity(new StartPage());
//                    break;
//            }
//        });
//    }
//
//    @Override
//    public String getActivityCommand() {
//        return "menu";
//    }
//
//    @Override
//    public CommandProcessor getProcessor() {
//        return processor;
//    }
//}