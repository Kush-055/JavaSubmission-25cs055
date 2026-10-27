package service;
import java.util.*;

public class TxnBuffer {
    private final Queue<Runnable> q = new ArrayDeque<>();
    private boolean done = false;

   public synchronized void put(Runnable r) {
        q.add(r);
        notifyAll();
    }

   public synchronized Runnable take() throws InterruptedException {
        while (q.isEmpty() && !done)
            wait();
        return q.poll();
    }

   public synchronized void finish() {
        done = true;
        notifyAll();
    }
}
