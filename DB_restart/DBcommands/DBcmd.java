package DBcommands;
import dataStruct.*;
import queryExceptions.*;
import java.io.File;
import java.util.*;

public abstract class DBcmd {
    String databaseName;
    String structureType;
    String alterationType;
    ArrayList<String> logicOperatorList;
    ArrayList<String> tableNames;
    ArrayList<String> attributeList;
    ArrayList<String> valueList;
    ArrayList<ArrayList<String>> conditionList;
    database DBToBeQueried;

    public void executeCMD() throws queryException {

    }

    public void checkDatabaseIsSelected() throws databaseNotSelectedException {
        if (DBToBeQueried == null) throw new databaseNotSelectedException();
    }

    public void setConditionList(ArrayList<String> condition){
        conditionList.add(condition);
    }

    public void setAlterationType(String alterationType) {
        this.alterationType = alterationType;
    }

    public void setAttribute(String attribute) throws queryException {
        if (!checkValidChars(attribute)) throw new queryException();
        attributeList.add(attribute);
    }

    public void setValue(String valueName) {
        valueList.add(valueName);
    }

    public void setDBToBeQueried(database DBToBeQueried) {
        this.DBToBeQueried = DBToBeQueried;
    }

    public void setDatabaseName(String DBname) {
        this.databaseName = DBname;
    }

    public void setStructureType(String structure) {
        this.structureType = structure;
    }

    public void setTableName(String tableName) throws queryException {
        if (!checkValidChars(tableName)) throw new queryException();
        tableNames.add(tableName);
    }

    public void setLogicOperator(String logicOperator) {
        logicOperatorList.add(logicOperator);
    }

    private Boolean checkValidChars(String token) {
        return token.matches("[a-zA-Z0-9]*$");
    }

    public Boolean checkConditions(ArrayList<String> row) throws invalidComparisonException {
        if (conditionList.isEmpty()) return true;
        ArrayList<ArrayList<String>> tableEntries = DBToBeQueried.getTable(tableNames.get(0)).getTableEntries();

        Stack<Boolean> resultStack = new Stack<>();
        for (ArrayList<String> condition : conditionList) {
            int colToBeTested = tableEntries.get(0).indexOf(condition.get(0));
            Boolean result = parseComparisonExpression(row.get(colToBeTested), condition.get(1), condition.get(2));
            resultStack.push(result);
        }
        for (int i = logicOperatorList.size() - 1; i >= 0; i--) {
            Boolean result1 = resultStack.pop();
            Boolean result2 = resultStack.pop();
            if (logicOperatorList.get(i).equals("AND")) resultStack.push(result1 && result2);
            else resultStack.push(result1 || result2);
        }
        return resultStack.pop();
    }

    public String getFilePath(String databaseName, String tableName) {
        return databaseName + File.separator + tableName + ".tsv";
    }

    public void checkAttributesExist() throws attributeDoesNotExistException {
        ArrayList<String> tableHeaders = DBToBeQueried.getTable(tableNames.get(0)).getTableHeaders();
        for (String attribute : attributeList) {
            if (!tableHeaders.contains(attribute)) throw new attributeDoesNotExistException();
        }
    }

    public void checkTablesExist() throws tableDoesNotExistException {
        for (String tableName : tableNames) {
            String tablePath = DBToBeQueried.getTablePath(tableName);
            File newTableFile = new File(tablePath);
            if (!newTableFile.exists()) throw new tableDoesNotExistException();
        }
    }

    private Boolean checkIfStringIsNumeric(String str) {
        try {
            Float.parseFloat(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    private void checkValuesAreSameType(String value1, String value2) throws invalidComparisonException {
        if (checkIfStringIsNumeric(value1) != checkIfStringIsNumeric(value2)) throw new invalidComparisonException();
    }

    private Boolean parseComparisonExpression(String string1, String operator, String string2) throws invalidComparisonException {
        checkValuesAreSameType(string1, string2);
        if (!checkIfStringIsNumeric(string1)) {
            Boolean result = string1.equals(string2);
            if (operator.equals("LIKE")) return string1.contains(string2);
            if (operator.equals("==")) return result;
            if (operator.equals("!=")) return !result;
            throw new invalidComparisonException();
        }
        float number1 = Float.parseFloat(string1);
        float number2 = Float.parseFloat(string2);
        switch(operator) {
            case ("<"):
                return number1 < number2;
            case (">"):
                return number1 > number2;
            case ("<="):
                return (number1 < number2 || compareFloat(number1, number2));
            case (">="):
                return (number1 > number2 || compareFloat(number1, number2));
            case ("=="):
                return compareFloat(number1, number2);
            case ("!="):
                return !compareFloat(number1, number2);
            default:
                return false;
            }
    }

    private Boolean compareFloat(float number1, float number2) {
        return (Math.abs(number1 - number2) < 0.001);
    }

    public void printTable(table table) {
        for (ArrayList<String> row : table.getTableEntries()) {
            for (String entry : row) {
                System.out.print(entry + "\t");
            }
            System.out.println();
        }
    }

}
