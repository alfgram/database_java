package DBcommands;
import dataStruct.*;
import queryExceptions.invalidComparisonException;
import queryExceptions.databaseNotSelectedException;
import queryExceptions.tableDoesNotExistException;
import queryExceptions.attributeDoesNotExistException;
import java.util.ArrayList;

public class selectCMD extends DBcmd {
    ArrayList<ArrayList<String>> tableEntries;

    public selectCMD(database database) {
        DBToBeQueried = database;
        attributeList = new ArrayList<>();
        tableNames = new ArrayList<>();
        conditionList = new ArrayList<>();
        logicOperatorList = new ArrayList<>();
    }

    public void executeCMD() throws databaseNotSelectedException, tableDoesNotExistException, attributeDoesNotExistException, invalidComparisonException {
        checkDatabaseIsSelected();
        checkTablesExist();
        checkAttributesExist();
        tableEntries = DBToBeQueried.getTable(tableNames.get(0)).getTableEntries();
        table selectTable = assembleTable();
        System.out.println("[OK]");
        printTable(selectTable);
    }

    private table assembleTable() throws invalidComparisonException {
        table newSelectTable = new table();
        assembleTableHeaders(newSelectTable);
        for (ArrayList<String> row : tableEntries.subList(1, tableEntries.size())) {
            ArrayList<String> selectTableRow = constructRow(row);
            if (!selectTableRow.isEmpty()) newSelectTable.setTableEntries(selectTableRow);
        }
        newSelectTable.removeFirstCol();
        return newSelectTable;
    }

    private void assembleTableHeaders(table selectTable) {
        if (attributeList.isEmpty()) attributeList = tableEntries.get(0);
        ArrayList<String> selectTableHeaders = new ArrayList<>();
        for (String header : tableEntries.get(0)) {
            if (attributeList.contains(header)) selectTableHeaders.add(header);
        }
        selectTable.setTableHeaders(selectTableHeaders);
    }

    private ArrayList<String> constructRow(ArrayList<String> row) throws invalidComparisonException {
        ArrayList<String> tableRow = new ArrayList<>();
        for (int i = 0; i < row.size(); i++) {
            if (attributeList.contains(tableEntries.get(0).get(i)) && checkConditions(row)) {
                tableRow.add(row.get(i));
            }
        }
        return tableRow;
    }
}