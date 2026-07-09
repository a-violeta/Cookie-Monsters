import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;

public class FileLogger implements Logger {
    private String prefix;
    private static final String FILE_PATH = "log.txt";
    LocalDate logDate = LocalDate.now();

    public FileLogger(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public void log(String message) {
        String formattedMessage = logDate + " / " + this.prefix + " / " + message;
        try (FileWriter fw = new FileWriter(FILE_PATH, true);
             PrintWriter pw = new PrintWriter(fw)) {
            pw.println(formattedMessage);
        } catch (IOException e) {
            System.out.println("Error Impossible d'écrire dans " + FILE_PATH);
        }
    }
}