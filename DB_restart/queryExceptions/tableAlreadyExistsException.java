package queryExceptions;

public class tableAlreadyExistsException extends queryException {

    public tableAlreadyExistsException() {
        super();
    }

    public String toString() {
        return "Table already Exists";
    }

}
