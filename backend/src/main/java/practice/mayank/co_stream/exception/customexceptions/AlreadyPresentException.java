package practice.mayank.co_stream.exception.customexceptions;

public class AlreadyPresentException extends RuntimeException {
    public AlreadyPresentException(String s) {
        super(s);
    }
}
