package DBcommands;
import dataStruct.database;
import queryExceptions.*;
import java.util.ArrayList;

public class alterCMD extends DBcmd {

    public alterCMD(database data) {
        DBToBeQueried = data;
        tableNames = new ArrayList<>();
        attributeList = new ArrayList<>();
    }

    public void executeCMD() throws tableDoesNotExistException, attributeDoesNotExistException, databaseNotSelectedException {
        checkDatabaseIsSelected();
        checkTablesExist();
        String tableName = tableNames.get(0);
        String attributeToBeAltered = attributeList.get(0);
        if (alterationType.equals("ADD")) {
            DBToBeQueried.getTable(tableName).addTableHeader(attributeToBeAltered);
        }
        else {
            checkAttributesExist();
            ArrayList<String> columnHeaders = DBToBeQueried.getTable(tableName).getTableHeaders();
            if (!columnHeaders.contains(attributeToBeAltered)) throw new attributeDoesNotExistException();
            DBToBeQueried.getTable(tableName).dropTableHeader(attributeToBeAltered);
        }
        DBToBeQueried.writeTableToFile(tableName);
        System.out.println("[OK]");
    }
}
