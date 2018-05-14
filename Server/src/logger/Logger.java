package logger;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;

public class Logger implements ILogger {

    private String repositoryName;

    public Logger(String repositoryName) {
        this.repositoryName = repositoryName;
    }

    @Override
    public void writeNewLog(String command) {
        File logFile = new File("repositories//" + repositoryName + "//" + String.format("%s.log", repositoryName));
        try {
            FileWriter fileWriter = new FileWriter(logFile, true);
            Date dateNow = new Date(System.currentTimeMillis());
            SimpleDateFormat formatForDateNow = new SimpleDateFormat("yyyy.MM.dd hh:mm:ss");
            fileWriter.write(formatForDateNow.format(dateNow) + " " + command + "\n");
            fileWriter.close();

        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public String getLog() throws IOException {
        File logFile = new File("repositories//" + repositoryName + "//" + String.format("%s.log", repositoryName));
        FileReader fileReader = new FileReader(logFile);
        StringBuilder result = new StringBuilder();
        int c;
        while ((c = fileReader.read()) != -1) result.append((char) c);
        return result.toString();
    }
}
