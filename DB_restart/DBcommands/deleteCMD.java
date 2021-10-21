package DBcommands;

import dataStruct.database;
import queryExceptions.attributeDoesNotExistException;
import queryExceptions.databaseNotSelectedException;
import queryExceptions.invalidComparisonException;
import queryExceptions.tableDoesNotExistException;
import java.util.ArrayList;

public class deleteCMD extends DBcmd {
    ArrayList<ArrayList<String>> tableToBeQueried;

    public deleteCMD(database data) {
        DBToBeQueried = data;
        tableNames = new ArrayList<>();
        logicOperatorList = new ArrayList<>();
        conditionList = new ArrayList<>();
    }

    public void executeCMD() throws tableDoesNotExistException, attributeDoesNotExistException, databaseNotSelectedException, invalidComparisonException {
        checkDatabaseIsSelected();
        checkTablesExist();
        deleteRowsIfConditionsMet();
        DBToBeQueried.writeTableToFile(tableNames.get(0));
        System.out.println("[OK]");
    }

    private void deleteRowsIfConditionsMet() throws invalidComparisonException {
        tableToBeQueried = DBToBeQueried.getTable(tableNames.get(0)).getTableEntries();
        int row = 1;
        while (row < tableToBeQueried.size()) {
            if (checkConditions(tableToBeQueried.get(row))) {
                DBToBeQueried.getTable(tableNames.get(0)).deleteRow(row);
            }
            else row++;
        }
    }
}