package pl.recommendations.exceptions;

public class FileReadException extends RuntimeException{
    public FileReadException(Throwable throwable) {
        super(throwable);
    }

    public FileReadException(String msg) {
        super(msg);
    }
}