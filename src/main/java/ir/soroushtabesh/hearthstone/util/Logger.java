package ir.soroushtabesh.hearthstone.util;

import ir.soroushtabesh.hearthstone.controllers.PlayerManager;
import ir.soroushtabesh.hearthstone.models.Log;
import ir.soroushtabesh.hearthstone.models.Player;
import ir.soroushtabesh.hearthstone.util.db.DBUtil;
import org.apache.log4j.Level;

import java.util.Date;

public class Logger {

    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(Logger.class);

    private Logger() {
    }

    public static void log(String event, String desc) {
        log(event, desc, Log.Severity.INFO);
    }

    public static void log(String event, String desc, Log.Severity severity) {
        Log log = new Log();
        if (PlayerManager.getInstance().getPlayer() != null) {
            Player player = PlayerManager.getInstance().getPlayer();
            log.setUser_id(player.getId());
            log.setUsername(player.getUsername());
        } else {
            log.setUser_id(-1);
        }
        log.setDate(new Date());
        log.setEvent(event);
        log.setDescription(desc);
        log.setSeverity(severity);
        if (Constants.isServerMode())
            DBUtil.doInJPATemp(session -> {
                session.saveOrUpdate(log);
                return null;
            });
        else
            logger.log(Level.ALL, log.toString());

    }
}
