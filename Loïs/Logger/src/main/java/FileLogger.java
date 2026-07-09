import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FileLogger implements Logger {

    private String prefix;

    private static final String FILE_PATH = "log.txt";

    //Create a Date Format excluding nanoseconds
    DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    public FileLogger(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public void log(String message) {

        //Get Local Date and Time
        LocalDateTime logDate = LocalDateTime.now();

        //Create a String of the Date with the created Format
        String formattedMessage = logDate.format(dateFormat) + " / " + this.prefix + " / " + message;

        try (FileWriter fw = new FileWriter(FILE_PATH, true);
             PrintWriter pw = new PrintWriter(fw)) {
            pw.println(formattedMessage);
            System.out.println(FILE_PATH);
        } catch (IOException e) {
            System.out.println("Error Impossible d'écrire dans " + FILE_PATH);
        }
    }
}