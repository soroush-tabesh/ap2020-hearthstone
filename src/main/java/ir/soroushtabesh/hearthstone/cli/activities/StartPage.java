package ir.soroushtabesh.hearthstone.cli.activities;

import ir.soroushtabesh.hearthstone.cli.CLIActivity;
import ir.soroushtabesh.hearthstone.cli.CommandProcessor;

public class StartPage extends CLIActivity {

    CommandProcessor processor = new CommandProcessor();

    public StartPage() {
    }

    @Override
    public void onStart(String[] args) {
        //todo
//        IRender render = new Render();
//        IContextBuilder builder = render.newBuilder();
//        builder.width(120).height(16);
//        builder.element(new Rectangle());
//        builder.element(new PseudoText("Hearth Stone",10,-1,16, new Font("Monospaced",Font.PLAIN,12),true));
//        builder.element(new Text("Soroush Tabesh, AP2020, Main Project Phase 1.",36,14,80,1));
//        ICanvas canvas = render.render(builder.build());
//        String s = canvas.getText();
//        System.out.println(s);
        if (args.length > 0 && args[0].equals("-a")) {

        }
        System.out.println("Hey there! Welcome.");
        //todo: initiate player and close session
    }

    @Override
    public void onStop() {
        System.out.println("Bye bye! Have a nice day buddy!");
        System.out.println("P.S. Heathstone is a real bullshit :)");
    }

    @Override
    public void onReceivedCommand(String[] args) {
        //todo
    }


    @Override
    public String getActivityCommand() {
        return "exit";
    }
}
