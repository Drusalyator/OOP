package fileWorker;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class FileWorker {

    private String path = null;
    private boolean isRecursive = false;

    public FileWorker(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isRecursive() {
        return isRecursive;
    }

    public void setRecursive(boolean flag) {
        isRecursive = flag;
    }

    public Map<String, String> execute(IExecutable executor) {
        File directory = new File(path);
        Map<String, String> result = new HashMap<>();
        doWork(directory, executor, result);
        return result;
    }

    private void doWork(File directory, IExecutable executor, Map<String, String> result) {
        for (File item : directory.listFiles()) {
            if (item.isDirectory()) {
                if (isRecursive) {
                    doWork(item.getAbsoluteFile(), executor, result);
                } else {
                    result.put(item.getName(), "directory");
                }
            } else {
                String executorResult = executor.process(item);
                if (executorResult != null) {
                    result.put(item.getName(), executorResult);
                }
            }
        }
    }
}
