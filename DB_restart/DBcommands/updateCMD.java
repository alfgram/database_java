package DBcommands;
import dataStruct.database;
import dataStruct.table;
import queryExceptions.*;
import java.util.ArrayList;

public class updateCMD extends DBcmd {
    table tableToBeQueried;
    public updateCMD(database data) {
        DBToBeQueried = data;
        tableNames = new ArrayList<>();
        attributeList = new ArrayList<>();
        valueList = new ArrayList<>();
        conditionList = new ArrayList<>();
        logicOperatorList = new ArrayList<>();
    }

    public void executeCMD() throws queryException {
        checkDatabaseIsSelected();
        checkTablesExist();
        tableToBeQueried = DBToBeQueried.getTable(tableNames.get(0));

        for (ArrayList<String> row : tableToBeQueried.getTableEntries()) {
            if (checkConditions(row)) {
                updateEntries(row);
            }
        }
        DBToBeQueried.writeTableToFile(tableNames.get(0));
        System.out.println("[OK]");
    }

    private void updateEntries(ArrayList<String> row)  {
        for (int i = 0; i < attributeList.size(); i++) {
            int columnToBeUpdated = tableToBeQueried.getTableHeaders().indexOf(attributeList.get(i));
            if (columnToBeUpdated >= row.size()) row.add(valueList.get(i));
            else row.set(columnToBeUpdated, valueList.get(i));
        }
    }
}
