package ir.soroushtabesh.hearthstone.util;

public class Constants {
    public static final String HIBERNATE_CFG = "hibernate.cfg.xml";
    public static final String DB_URL_PREFIX = "jdbc:postgresql:";
    public static final String DB_URL_SUFFIX = "";
    public static final String DB_URL = "//localhost:5432/soroush_tabesh_hs";

    private static boolean serverMode = false;

    public static boolean isServerMode() {
        return serverMode;
    }

    public static void setServerMode(boolean serverMode) {
        Constants.serverMode = serverMode;
    }

    private Constants() {
    }
}
