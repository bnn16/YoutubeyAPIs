package nl.fontys.youtubeyspringapi.exception;

public class EmptyPostListException extends Exception {

    private String message;

    public EmptyPostListException(String message) {
        super(message);
        this.message = message;
    }

    public EmptyPostListException() {
    }
}
