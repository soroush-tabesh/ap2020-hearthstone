package ir.soroushtabesh.hearthstone.util;

import ir.soroushtabesh.hearthstone.controllers.PlayerManager;
import ir.soroushtabesh.hearthstone.db.DBUtil;
import ir.soroushtabesh.hearthstone.models.beans.Log;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
    public static void log(String event, String desc) {
        log(event, desc, Log.Severity.INFO);
    }

    public static void log(String event, String desc, Log.Severity severity) {
        Log log = new Log();
        if (PlayerManager.getInstance().getPlayer() != null)
            log.setUser_id(PlayerManager.getInstance().getPlayer().getUser_id());
        else
            log.setUser_id(-1);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        log.setDate(dtf.format(now));
        log.setEvent(event);
        log.setDescription(desc);
        log.setSeverity(severity);
        DBUtil.saveSingleObject(log);
    }
}
