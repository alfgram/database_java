package queryExceptions;

public class attributeDoesNotExistException extends queryException {

    public attributeDoesNotExistException() {
        super();
    }

    public String toString() {
        return "Attribute does not exist";
    }

}
