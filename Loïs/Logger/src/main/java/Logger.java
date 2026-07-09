//Interface, declaration of a contract
public interface Logger {

    //log method needed in each Logger SubClass
    void log(LogLevel logLevel, String message);
}