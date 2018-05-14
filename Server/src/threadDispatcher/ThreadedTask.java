package threadDispatcher;

abstract class ThreadedTask implements Runnable {

    String name;

    @Override
    public void run() {
        doWork();
        finishThread();
    }

    abstract void doWork();

    private void finishThread() {
        ThreadDispatcher threadDispatcher = ThreadDispatcher.getInstance();
        threadDispatcher.deleteThread(Thread.currentThread().getId());
    }


}
