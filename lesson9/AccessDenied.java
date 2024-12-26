package lesson9;

public class AccessDenied extends RuntimeException {
    public AccessDenied() {

    }
    public AccessDenied(String message) {
        super(message);
    }
}
