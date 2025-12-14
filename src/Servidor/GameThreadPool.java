package Servidor;
import java.util.LinkedList;
import java.util.Queue;

public class GameThreadPool {
	
	private final int maxWorkers;
	private Queue<Runnable> taskQueue = new LinkedList<>();
	private Worker[] workers;
	
	public GameThreadPool(int maxWorkers) {
        this.maxWorkers = maxWorkers;
        this.workers = new Worker[maxWorkers];

        for (int i = 0; i < maxWorkers; i++) {
            workers[i] = new Worker();
            workers[i].start();
        }
    }

    public synchronized void submit(Runnable task) {
        taskQueue.add(task);
        notifyAll();
    }

    private synchronized Runnable getTask() throws InterruptedException {
        while (taskQueue.isEmpty()) {
            wait();
        }
        return taskQueue.poll();
    }

    private class Worker extends Thread {
        public void run() {
            while (true) {
                try {
                    Runnable task = getTask();
                    task.run(); //corre a GameThread
                } catch (InterruptedException e) {
                    break;
                }
            }
        }
    }


}
