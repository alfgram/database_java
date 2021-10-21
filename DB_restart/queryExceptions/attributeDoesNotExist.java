package queryExceptions;

public class attributeDoesNotExist extends queryException {

    public attributeDoesNotExist() {
        super();
    }

    public String toString() {
        return "Attribute does not exist";
    }

}
