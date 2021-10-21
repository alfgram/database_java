package queryExceptions;

public class tableDoesNotExistException extends queryException {

    public tableDoesNotExistException() {
        super();
    }

    public String toString() {
        return "Table does not exist";
    }

}
