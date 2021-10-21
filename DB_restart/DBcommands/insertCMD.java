package DBcommands;
import dataStruct.*;
import queryExceptions.databaseNotSelectedException;
import queryExceptions.tableDoesNotExistException;

import java.util.ArrayList;

public class insertCMD extends DBcmd {

    public insertCMD(database database) {
        DBToBeQueried = database;
        tableNames = new ArrayList<>();
        valueList = new ArrayList<>();
    }

    public void executeCMD() throws databaseNotSelectedException, tableDoesNotExistException {
        checkDatabaseIsSelected();
        checkTablesExist();
        DBToBeQueried.getTable(tableNames.get(0)).setTableEntries(valueList);
        DBToBeQueried.writeTableToFile(tableNames.get(0));
        System.out.println("[OK]");
    }
}
