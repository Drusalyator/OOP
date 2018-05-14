package threadDispatcher;

import java.util.HashMap;
import java.util.Map;

public class ThreadDispatcher {

    private static volatile ThreadDispatcher instance;
    private Map<Long, String> workingThread;
    private ThreadMonitor treadMonitor;

    private ThreadDispatcher() {
        workingThread = new HashMap<>();
        treadMonitor = new ThreadMonitor();
        Add(treadMonitor);
    }

    public static ThreadDispatcher getInstance() {
        if (instance == null)
            synchronized (ThreadDispatcher.class) {
                if (instance == null)
                    instance = new ThreadDispatcher();
            }
        return instance;
    }

    Map<Long, String> getWorkingThread() {
        return workingThread;
    }

    public void Add(ThreadedTask threadedTask) {
        Thread thread = new Thread(threadedTask);
        thread.start();
        workingThread.put(thread.getId(), threadedTask.name);
        System.out.println(workingThread);
        treadMonitor.needUpdate();
    }

    synchronized void deleteThread(long threadId) {
        workingThread.remove(threadId);
        System.out.println(workingThread);
        treadMonitor.needUpdate();
    }

    public void Stop() {
        treadMonitor.stopWork();
    }
}
