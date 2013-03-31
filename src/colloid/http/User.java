package colloid.http;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import colloid.RecountApp;
import colloid.model.LogUtil;
import colloid.model.event.Actor;
import colloid.model.event.DoesNotExist;
import colloid.model.event.RecountLoop;
import colloid.model.event.Util;
import colloid.model.event.RecountLoop.InvalidCombatDirPathException;

public class User {

    protected Actor me = null;

    protected static User instance = null;
    protected static boolean tryFindUser = false;

    public User() {
    }

    public static User getInstance() {
        if (instance == null) {
            instance = new User();
        }

        return instance;
    }

    public void login(String logdata) {
        if (logdata.contains("Safe Login")) {
            try {
                Actor actor = new Actor(logdata);
                //if not companion
                if (!actor.getName().contains(":")) {
                    me = actor;
                }
            } catch (DoesNotExist e) {
                //do nothing
            }
        }

    }

    public Actor tryFindUserInLogFile() throws InvalidCombatDirPathException, UnloggedUserError {

        if (tryFindUser) {
            throw new UnloggedUserError();
        }
        String path = RecountApp.getInstance().getCombatDirPath();
        if (path == null) {
            tryFindUser = true;
            throw new UnloggedUserError();
        }
        File dir = new File(path);
        File[] logs = dir.listFiles();
        if (logs == null || logs.length == 0) {
            RecountApp.getInstance().new InvalidCombatDirPathException();
        }
        File log = logs[logs.length - 1];
        String tailLines;
        try {
            tailLines = LogUtil.tail(log, 30);
        } catch (StringIndexOutOfBoundsException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, "Invalid log combat source");
            throw new UnloggedUserError();
        }
        String[] lines = tailLines.split("\n");
        for (String line: lines) {
            if (line.contains("Safe Login")) {
                login(line);
                if (isLogged()) {
                    break;
                }
            }
        }
        tryFindUser = true;
        if (isLogged()) {
            return me;
        }
        throw new UnloggedUserError();
    }

    public boolean isLogged() {
        return (me != null);
    }

    public Actor me() throws UnloggedUserError {
        if (!isLogged()) {
            try {
                return tryFindUserInLogFile();
            } catch (InvalidCombatDirPathException e) {
                Logger.getLogger(User.class.getName()).log(Level.SEVERE, "Invlid logged user");
                throw new UnloggedUserError();
            }
        }
        return me;
    }

    public class UnloggedUserError extends Exception {

        private static final long serialVersionUID = 1L;
    }
}
