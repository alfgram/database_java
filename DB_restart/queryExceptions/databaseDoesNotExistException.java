package queryExceptions;

public class databaseDoesNotExistException extends queryException {

    public databaseDoesNotExistException() {
        super();
    }

    public String toString() {
        return "Database does not exist";
    }

}
