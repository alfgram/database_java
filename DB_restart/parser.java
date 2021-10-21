import DBcommands.*;
import dataStruct.database;
import queryExceptions.*;

import java.util.ArrayList;

public class parser {
    tokenizer query;
    database currentDatabase;
    DBcmd userCommand;

    public DBcmd parseQuery(String commandToParse) throws queryException {
        if (commandToParse.isEmpty()) throw new queryException();
        query = new tokenizer(commandToParse);
        switch (query.getFirstToken()) {
            case "USE": {
                userCommand = new useCMD();
                this.useQuery();
                break;
            }
            case "CREATE": {
                userCommand = new createCMD();
                this.createQuery();
                break;
            }
            case "INSERT": {
                userCommand = new insertCMD(currentDatabase);
                this.insertQuery();
                break;
            }
            case "DROP": {
                userCommand = new dropCMD(currentDatabase);
                this.dropQuery();
                break;
            }
            case "SELECT": {
                userCommand = new selectCMD(currentDatabase);
                this.selectQuery();
                break;
            }
            case "ALTER": {
                userCommand = new alterCMD(currentDatabase);
                this.alterQuery();
                break;
            }
            case "DELETE": {
                userCommand = new deleteCMD(currentDatabase);
                this.deleteQuery();
                break;
            }
            case "JOIN": {
                userCommand = new joinCMD(currentDatabase);
                this.joinQuery();
                break;
            }
            case "UPDATE": {
                userCommand = new updateCMD(currentDatabase);
                this.updateQuery();
                break;
            }
            default: {
                throw new queryException();
            }
        }
        if (query.hasMoreTokens()) throw new queryException();
        return userCommand;
    }

    private void updateQuery() throws queryException {
        userCommand.setTableName(query.nextToken());
        if (!query.nextToken().equals("SET")) throw new queryException();
        nameValuePairList();
        parseConditions();
    }

    private void selectQuery() throws queryException {
        String token = query.nextToken();
        if (token.equals("*")) token = query.nextToken();
        else {
            userCommand.setAttribute(token);
            do {
                token = query.nextToken();
                if (token.equals(",")) userCommand.setAttribute(query.nextToken());
            } while (token.equals(","));
        }

        if (!token.equals("FROM")) throw new queryException();
        userCommand.setTableName(query.nextToken());
        if (!query.hasMoreTokens()) return;
        if (!query.nextToken().equals("WHERE")) throw new queryException();
        parseConditions();
    }

    private void joinQuery() throws queryException {
        userCommand.setTableName(query.nextToken());
        if (!query.nextToken().equals("AND")) throw new queryException();
        userCommand.setTableName(query.nextToken());
        if (!query.nextToken().equals("ON")) throw new queryException();
        userCommand.setAttribute((query.nextToken()));
        if (!query.nextToken().equals("AND")) throw new queryException();
        userCommand.setAttribute((query.nextToken()));
    }

    private void deleteQuery() throws queryException {
        if (!query.nextToken().equals("FROM")) throw new queryException();
        userCommand.setTableName((query.nextToken()));
        if (!query.nextToken().equals("WHERE")) throw new queryException();
        parseConditions();
    }

    private void alterQuery() throws queryException {
        if (!query.nextToken().equals("TABLE")) throw new queryException();
        userCommand.setTableName(query.nextToken());
        String alterationType = query.nextToken();
        if (!alterationType.equals("ADD") && !alterationType.equals("DROP")) throw new queryException();
        userCommand.setAlterationType(alterationType);
        userCommand.setAttribute(query.nextToken());
    }

    private void useQuery() throws queryException {
        currentDatabase = new database();
        currentDatabase.setDatabaseName(query.nextToken());
        userCommand.setDBToBeQueried(currentDatabase);
    }

    private void dropQuery() throws queryException {
        String structureType = query.nextToken();
        if (structureType.equals("DATABASE")) {
            userCommand.setStructureType("DATABASE");
            userCommand.setDatabaseName(query.nextToken());
        }
        else if (structureType.equals("TABLE")) {
            userCommand.setStructureType("TABLE");
            userCommand.setDBToBeQueried(currentDatabase);
            userCommand.setTableName(query.nextToken());
        }
        else throw new queryException();
    }

    private void createQuery() throws queryException {
        String structureType = query.nextToken();
        if (structureType.equals("DATABASE")) {
            userCommand.setStructureType("DATABASE");
            userCommand.setDatabaseName(query.nextToken());
        }
        else if (structureType.equals("TABLE")) {
            userCommand.setStructureType("TABLE");
            userCommand.setDBToBeQueried(currentDatabase);
            userCommand.setTableName(query.nextToken());
            if (query.hasMoreTokens()) {
                parseValueList();
            }
        }
        else throw new queryException();
    }

    private void insertQuery() throws queryException {
        if (!query.nextToken().equals("INTO")) throw new queryException();
        userCommand.setTableName(query.nextToken());
        if (!query.nextToken().equals("VALUES")) throw new queryException();
        parseValueList();
    }

    private void parseConditions() throws queryException {
        ArrayList<String> condition = new ArrayList<>();
        String token = query.nextToken();
        while (token.equals("(")) {
            token = query.nextToken();
        }
        condition.add(token);
        parseOperator(condition);
        condition.add(query.nextToken());
        userCommand.setConditionList(condition);
        if (!query.hasMoreTokens()) return;
        token = query.nextToken();
        while (token.equals(")") && query.hasMoreTokens()) {
            token = query.nextToken();
        }
        if (!query.hasMoreTokens()) return;
        String logicOperator = token;
        if (!logicOperator.equals("OR") && !logicOperator.equals("AND")) throw new queryException();
        userCommand.setLogicOperator(logicOperator);
        parseConditions();
    }

    private void parseOperator(ArrayList<String> conditions) throws queryException {
        String operator = query.nextToken();
        if (operator.equals("==") || operator.equals(">") || operator.equals("<") ||
                operator.equals(">=") || operator.equals("<=") || operator.equals("!=") ||
                operator.equals("LIKE")) {
            conditions.add(operator);
            return;
        }
        throw new queryException();
    }

    private void parseValueList() throws queryException {
        if (!query.nextToken().equals("(")) throw new queryException();
        userCommand.setValue(query.nextToken());
        String token;
        do {
            token = query.nextToken();
            if (token.equals(",")) userCommand.setValue(query.nextToken());
            else if (token.equals(")")) return;
            else throw new queryException();
        } while (true);
    }

    private void nameValuePairList() throws queryException {
        userCommand.setAttribute(query.nextToken());
        if (!query.nextToken().equals("=")) throw new queryException();
        userCommand.setValue(query.nextToken());
        String token = query.nextToken();
        while (token.equals(",")) {
            userCommand.setAttribute(query.nextToken());
            if (!query.nextToken().equals("=")) throw new queryException();
            userCommand.setValue(query.nextToken());
            token = query.nextToken();
        }
        if (!token.equals("WHERE")) throw new queryException();
    }
}