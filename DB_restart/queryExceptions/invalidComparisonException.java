package queryExceptions;

public class invalidComparisonException extends queryException {

    public invalidComparisonException() {
        super();
    }

    public String toString() {
        return "Invalid comparison of two strings";
    }

}
