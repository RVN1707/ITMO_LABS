package exceptions;

public class NotGutter extends Exception {
    private final String message;
    public NotGutter(String m){
       message=m ;
    }
    @Override
    public String getMessage() {
        return " NotGutterException: " + message ;
    }
    @Override
    public String toString() {
        return getMessage();
    }
}