package queryExceptions;

public class cannotDeleteFileException extends queryException {

    public cannotDeleteFileException() {
        super();
    }

    public String toString() {
        return "Unable to delete file";
    }

}
