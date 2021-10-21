package queryExceptions;

public class databaseAlreadyExistsException extends queryException {

    public databaseAlreadyExistsException() {
        super();
    }

    public String toString() {
        return "Database already Exists";
    }

}
