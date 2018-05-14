package threadDispatcher;

import java.io.FileWriter;
import java.io.IOException;

public class ThreadMonitor extends ThreadedTask {

    private volatile boolean needUpdate = false;
    private volatile boolean workingFlag = true;

    ThreadMonitor() {
        name = "ThreadMonitor";
    }

    @Override
    public void doWork() {
        ThreadDispatcher threadDispatcher = ThreadDispatcher.getInstance();
        while (workingFlag) {
            try {
                if (needUpdate)
                    writeThreads(threadDispatcher);
                needUpdate = false;
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }

    private void writeThreads(ThreadDispatcher threadDispatcher) throws IOException {
        FileWriter fileWriter = new FileWriter("WorkingThreads.txt");
        fileWriter.write(String.valueOf(threadDispatcher.getWorkingThread()));
        fileWriter.close();
    }

    void needUpdate() {
        needUpdate = true;
    }

    void stopWork() {
        workingFlag = false;
    }
}
