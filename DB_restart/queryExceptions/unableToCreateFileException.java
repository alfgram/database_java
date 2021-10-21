package queryExceptions;

public class unableToCreateFileException extends queryException {

    public unableToCreateFileException() {
        super();
    }

    public String toString() {
        return "Unable to create database/table";
    }

}
