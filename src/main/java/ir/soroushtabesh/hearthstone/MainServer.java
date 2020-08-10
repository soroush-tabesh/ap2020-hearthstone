package ir.soroushtabesh.hearthstone;

import ir.soroushtabesh.hearthstone.util.Constants;
import ir.soroushtabesh.hearthstone.util.db.Seeding;

public class MainServer {
    public static void main(String[] args) {
        Constants.setServerMode(true);
        Seeding.initiate();

    }
}
