package queryExceptions;

public class databaseNotSelectedException extends queryException {

    public databaseNotSelectedException() {
        super();
    }

    public String toString() {
        return "No database selected";
    }

}
