package dataStruct;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class table {
    int id = 1;
    private ArrayList<ArrayList<String>> tableEntries = new ArrayList<>();

    public void loadTableFromTSV(String filePath) {
        File fileToLoad = new File(filePath);
        if (fileToLoad.length() == 0) {
            return;
        }
        try {
            FileReader reader = new FileReader(fileToLoad);
            BufferedReader buffReader = new BufferedReader(reader);
            String line = buffReader.readLine();
            loadTableHeaders(line);
            loadTableEntries(buffReader);
        }
        catch (IOException ioe) {
            System.out.println("[ERROR] problem reading file");
        }
    }

    private void loadTableEntries(BufferedReader b) throws IOException {
        String line;
        while ((line = b.readLine()) != null) {
            ArrayList<String> lineEntries = new ArrayList<>(Arrays.asList(line.split("\\t")));
            tableEntries.add(lineEntries);
        }
        b.close();
    }

    private void loadTableHeaders(String headersString) {
        ArrayList<String> tableHeaders = new ArrayList<>(Arrays.asList(headersString.split("\\t")));
        tableEntries.add(tableHeaders);
    }

    public ArrayList<ArrayList<String>> getTableEntries() {
        return tableEntries;
    }

    public ArrayList<String> getTableHeaders() {
        return tableEntries.get(0);
    }

    public void setTableHeaders(ArrayList<String> header) {
        header.add(0, "id");
        tableEntries.add(header);
    }

    public void setTableEntries(ArrayList<String> entry) {
        entry.add(0, String.valueOf(id++));
        tableEntries.add(entry);
    }

    public void addTableHeader(String tableHeader) {
        tableEntries.get(0).add(tableHeader);
    }

    public void dropTableHeader(String tableHeader) {
        int indexOfColumnToDrop = getTableHeaders().indexOf(tableHeader);
        for (ArrayList<String> row : tableEntries) {
            if (indexOfColumnToDrop < row.size()) row.remove(indexOfColumnToDrop);
        }
    }
    public void removeFirstCol() {
        for (ArrayList<String> row : tableEntries) {
            row.remove(0);
        }
    }
    public void deleteRow(int rowIndex) {
        tableEntries.remove(rowIndex);
    }
}
