package DBcommands;
import queryExceptions.databaseDoesNotExistException;

import java.io.File;

public class useCMD extends DBcmd {

    public void executeCMD() throws databaseDoesNotExistException {
        File databaseDirectory = new File(DBToBeQueried.getDatabaseName());
        if (!databaseDirectory.isDirectory()) throw new databaseDoesNotExistException();
        DBToBeQueried.loadTablesIntoDatabase(DBToBeQueried.getDatabaseName());
        System.out.println("[OK]");
    }
}
