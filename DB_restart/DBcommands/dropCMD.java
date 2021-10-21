package DBcommands;
import dataStruct.database;
import queryExceptions.databaseAlreadyExistsException;
import queryExceptions.databaseNotSelectedException;
import queryExceptions.databaseDoesNotExistException;
import queryExceptions.cannotDeleteFileException;
import java.io.File;
import java.util.ArrayList;

public class dropCMD extends DBcmd {

    public dropCMD(database data) {
        DBToBeQueried = data;
        tableNames = new ArrayList<>();
    }

    public void executeCMD() throws databaseAlreadyExistsException, databaseNotSelectedException, databaseDoesNotExistException, cannotDeleteFileException {
        if (structureType.equals("DATABASE")) {
            File databaseToDelete = new File(databaseName);
            if (!databaseToDelete.isDirectory()) throw new databaseDoesNotExistException();
            deleteDatabase(databaseToDelete);
        }
        else if (structureType.equals("TABLE")) {
            checkDatabaseIsSelected();
            File tableToDelete = new File(getFilePath(DBToBeQueried.getDatabaseName(), tableNames.get(0)));
            if (!tableToDelete.delete()) throw new cannotDeleteFileException();
        }
        System.out.println("[OK]");
    }

    private void deleteDatabase(File databaseToDelete) throws cannotDeleteFileException {
        File[] tablesInDatabase = databaseToDelete.listFiles();
        if (tablesInDatabase != null) {
            for (File file : tablesInDatabase) {
                if (!file.delete()) throw new cannotDeleteFileException();
            }
        }
        if (!databaseToDelete.delete()) throw new cannotDeleteFileException();
    }
}
