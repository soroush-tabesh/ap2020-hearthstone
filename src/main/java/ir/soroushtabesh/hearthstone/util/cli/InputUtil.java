package ir.soroushtabesh.hearthstone.util.cli;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputUtil {

    public static String[] tokenize(String string) {
        ArrayList<String> tokens = new ArrayList<>();
        Pattern pattern = Pattern.compile("(\"((\\\\\\\\)|(\\\\\")|.)*?(\"|$))|(\\S+)");
        Matcher matcher = pattern.matcher(string);
        while (matcher.find()) {
            tokens.add(matcher.group().replaceAll("(^\")|(\"$)", ""));
        }
        String[] strings = new String[tokens.size()];
        for (int i = 0; i < tokens.size(); i++) {
            strings[i] = tokens.get(i);
        }
        return strings;
    }
}
