package ir.soroushtabesh.hearthstone.util.cli;

import java.util.Collection;

public class PrintUtil {

    public static void printList(Collection list) {
        for (Object object : list) {
            printFormat(object);
        }
    }

    public static void printFormat(Object object) {
        System.out.println(">>");
        System.out.println(object);
        System.out.println();
    }
}
