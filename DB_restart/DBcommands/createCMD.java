package DBcommands;
import dataStruct.*;
import queryExceptions.databaseAlreadyExistsException;
import queryExceptions.databaseNotSelectedException;
import queryExceptions.unableToCreateFileException;
import java.io.File;
import java.util.ArrayList;

public class createCMD extends DBcmd {

    public createCMD() {
        valueList = new ArrayList<>();
        tableNames = new ArrayList<>();
    }

    public void executeCMD() throws databaseAlreadyExistsException, databaseNotSelectedException, unableToCreateFileException {
        if (structureType.equals("DATABASE")) {
            createDatabase();
        }
        else if (structureType.equals("TABLE")) {
            checkDatabaseIsSelected();
            createTable();
        }
        System.out.println("[OK]");
    }

    private void createDatabase() throws unableToCreateFileException, databaseAlreadyExistsException {
        File newDatabase = new File(databaseName);
        if (newDatabase.isDirectory()) throw new databaseAlreadyExistsException();
        if (!newDatabase.mkdir()) throw new unableToCreateFileException();
    }

    private void createTable() throws unableToCreateFileException {
        table newTable = new table();
        newTable.setTableHeaders(valueList);
        DBToBeQueried.addTable(tableNames.get(0), newTable);
    }
}
