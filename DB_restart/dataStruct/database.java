package dataStruct;
import queryExceptions.unableToCreateFileException;
import java.io.*;
import java.util.ArrayList;
import java.util.Hashtable;

public class database {
    Hashtable<String, table> tableList = new Hashtable<>();
    protected String databaseName;

    public void loadTablesIntoDatabase(String name) {
        databaseName = name;
        tableList.clear();
        File databaseFile = new File(databaseName);
        File[] tablesInDatabase = databaseFile.listFiles();
        if (tablesInDatabase == null) {
            return;
        }
        for (File table : tablesInDatabase) {
            table newTable = new table();
            newTable.loadTableFromTSV(getTablePath(removeFileExtension(table.getName())));
            tableList.put(removeFileExtension(table.getName()), newTable);
        }
    }

    public void writeTableToFile(String tableName) {

        try {
            String tableFilePath = getTablePath(tableName);
            File filename = new File(tableFilePath);
            table tableToWrite = this.getTable(tableName);
            clearFile(tableFilePath);
            FileWriter fw = new FileWriter(filename);
            BufferedWriter writer = new BufferedWriter(fw);
            writeToFile(writer, tableToWrite);
            writer.flush();
            writer.close();
        } catch(IOException ioe) {
            System.out.println(ioe);
        }
    }

    private void writeToFile(BufferedWriter writer, table tableToWrite) throws IOException {
        ArrayList<ArrayList<String>> tableEntries = tableToWrite.getTableEntries();
        if (!tableEntries.isEmpty()) {
            for (ArrayList<String> line : tableEntries) {
                for (String entry : line) {
                    writer.write(entry + "\t");
                }
                writer.newLine();
            }
        }

    }

    private void clearFile(String tableFilePath) throws FileNotFoundException {
        PrintWriter pw = new PrintWriter(tableFilePath);
        pw.close();
    }

    public void addTable(String tableName, table newTable) throws unableToCreateFileException {
        File newFile = new File(getTablePath(tableName));
        try {
            if (!newFile.createNewFile()) throw new unableToCreateFileException();
            tableList.put(tableName, newTable);
            writeTableToFile(tableName);
        } catch(IOException ioe) {
            System.out.println("[ERROR] Unable to create file, check permissions");
        }
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getTablePath(String tableName) {
        return databaseName + File.separator + tableName + ".tsv";
    }

    public table getTable(String tableName){
        return tableList.get(tableName);
    }

    private String removeFileExtension(String fileName) {
        return fileName.substring(0, fileName.lastIndexOf('.'));
    }
}
