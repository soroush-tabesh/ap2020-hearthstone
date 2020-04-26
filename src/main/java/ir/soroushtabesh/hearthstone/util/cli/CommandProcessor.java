package ir.soroushtabesh.hearthstone.util.cli;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class CommandProcessor {

    private Map<String, ProcessListener> processListenerMap = new TreeMap<>();

    public void add(String activityCommand, ProcessListener processListener) {
        processListenerMap.put(activityCommand, processListener);
    }

    /**
     * @param s - delimited input command
     * @return true if command is successfully handled
     */
    public boolean process(String[] s) {
        if (s.length == 0)
            return false;
        ProcessListener processListener = processListenerMap.get(s[0]);
        if (processListener == null)
            return false;
        processListener.run(new ProcessListenerEvent(s[0], Arrays.copyOfRange(s, 1, s.length)));
        return true;
    }

    public Set<String> getCommandList() {
        return processListenerMap.keySet();
    }

    public void clearCommandList() {
        processListenerMap.clear();
    }
}
