package pl.uam.wmi.niezbednikstudenta.exceptions;

public class LoginException extends Exception {

    public LoginException(String message) {
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
