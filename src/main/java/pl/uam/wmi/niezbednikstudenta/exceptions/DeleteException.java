package pl.uam.wmi.niezbednikstudenta.exceptions;

public class DeleteException extends Exception {

    public DeleteException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }

    @Override
    public synchronized Throwable getCause() {
        return super.getCause();
    }
}
