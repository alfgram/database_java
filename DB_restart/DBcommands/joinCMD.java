package DBcommands;
import dataStruct.*;
import queryExceptions.attributeDoesNotExistException;
import queryExceptions.databaseNotSelectedException;
import queryExceptions.tableDoesNotExistException;
import java.util.ArrayList;

public class joinCMD extends DBcmd {

    public joinCMD(database data) {
        DBToBeQueried = data;
        attributeList = new ArrayList<>();
        tableNames = new ArrayList<>();
    }

    public void executeCMD() throws tableDoesNotExistException, attributeDoesNotExistException, databaseNotSelectedException {
        checkDatabaseIsSelected();
        checkTablesExist();
        table joinedTable = new table();

        ArrayList<ArrayList<String>> tableOneEntries = DBToBeQueried.getTable(tableNames.get(0)).getTableEntries();
        ArrayList<ArrayList<String>> tableTwoEntries = DBToBeQueried.getTable(tableNames.get(1)).getTableEntries();

        int tableOneIndex = tableOneEntries.get(0).indexOf(attributeList.get(0));
        int tableTwoIndex = tableTwoEntries.get(0).indexOf(attributeList.get(1));
        ArrayList<String> joinedTableHeaders = new ArrayList<>();
        joinedTableHeaders.addAll(formatTableHeaders(tableOneEntries.get(0), tableNames.get(0)));
        joinedTableHeaders.addAll(formatTableHeaders(tableTwoEntries.get(0), tableNames.get(1)));
        joinedTable.setTableHeaders(joinedTableHeaders);

        for (ArrayList<String> tableOneRow : tableOneEntries) {
            for (ArrayList<String> tableTwoRow : tableTwoEntries) {
                if (tableOneRow.get(tableOneIndex).equals(tableTwoRow.get(tableTwoIndex))) {
                    joinedTable.setTableEntries(joinTableRows(tableOneRow, tableTwoRow));
                }
            }
        }
        System.out.println("[OK]");
        printTable(joinedTable);
    }

    private ArrayList<String> joinTableRows(ArrayList<String> rowOne, ArrayList<String> rowTwo) {
        ArrayList<String> joinedTableRow = new ArrayList<>();
        ArrayList<String> table1FormattedRow = new ArrayList<>(rowOne);
        ArrayList<String> table2FormattedRow = new ArrayList<>(rowTwo);
        table1FormattedRow.remove(0);
        table2FormattedRow.remove(0);
        joinedTableRow.addAll(table1FormattedRow);
        joinedTableRow.addAll(table2FormattedRow);
        return joinedTableRow;
    }



    private ArrayList<String> formatTableHeaders(ArrayList<String> headers, String tableName) {
        ArrayList<String> formattedTableHeaders = new ArrayList<>();
        for (int i = 1; i < headers.size(); i++) {
            formattedTableHeaders.add(tableName + "." + headers.get(i));
        }
        return formattedTableHeaders;
    }
}
