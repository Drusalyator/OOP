package logger;

import java.io.IOException;

public interface ILogger {

    void writeNewLog(String command);

    String getLog() throws IOException;
}
