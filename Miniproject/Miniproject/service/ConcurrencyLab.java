package service;

import java.util.concurrent.*;
import model.Account;
import model.CurrentAccount;

public class ConcurrencyLab {
   public static void producerConsumerDemo() {
        TxnBuffer b = new TxnBuffer();
        Thread producer = new Thread(() -> {
            for (int i = 1; i <= 3; i++) {
                final int x = i;
                b.put(() -> System.out.println("processed txn " + x));
            }
            b.finish();
        }, "producer");
        Thread consumer = new Thread(() -> {
            try {
                Runnable r;
                while ((r = b.take()) != null)
                    r.run();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "consumer");
        producer.start();
        consumer.start();
        try {
            producer.join();
            consumer.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Demonstrates Practical 9: Unsynchronized balance updates leading to race condition,
     * contrasted with thread-safe synchronized execution.
     */
    public static void raceConditionDemo() {
        class UnsafeAccount {
            long balance = 0;
            void deposit(long amt) {
                long temp = balance;
                Thread.yield();
                balance = temp + amt;
            }
        }
        UnsafeAccount unsafe = new UnsafeAccount();
        Thread[] ts = new Thread[10];
        for (int i = 0; i < ts.length; i++) {
            ts[i] = new Thread(() -> {
                for (int j = 0; j < 1000; j++) unsafe.deposit(1);
            });
        }
        for (Thread t : ts) t.start();
        for (Thread t : ts) {
            try { t.join(); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        }
        System.out.println("Unsynchronized balance (race condition): " + unsafe.balance + " (expected < 10000)");

        Account safe = new CurrentAccount("Safe", 0, 0);
        for (int i = 0; i < ts.length; i++) {
            ts[i] = new Thread(new AccountWorker(safe, 1000, 1));
        }
        for (Thread t : ts) t.start();
        for (Thread t : ts) {
            try { t.join(); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        }
        System.out.println("Synchronized balance (thread-safe): " + safe.getBalance() + " (expected 10000)");
    }

    /**
     * Demonstrates Practical 10: Opposite lock order deadlock demonstration with timeout
     * and resolution via natural account number ordering.
     */
    public static void deadlockDemo() {
        Account deadA = new CurrentAccount("DeadA", 1000, 0);
        Account deadB = new CurrentAccount("DeadB", 1000, 0);
        CountDownLatch ready = new CountDownLatch(2);

        Thread t1 = new Thread(() -> {
            synchronized (deadA) {
                ready.countDown();
                try { Thread.sleep(50); } catch (InterruptedException ignored) {}
                synchronized (deadB) {
                }
            }
        }, "deadlock-thread-1");
        t1.setDaemon(true);

        Thread t2 = new Thread(() -> {
            synchronized (deadB) {
                ready.countDown();
                try { Thread.sleep(50); } catch (InterruptedException ignored) {}
                synchronized (deadA) {
                }
            }
        }, "deadlock-thread-2");
        t2.setDaemon(true);

        t1.start();
        t2.start();

        try {
            ready.await(200, TimeUnit.MILLISECONDS);
            Thread.sleep(100);
            if (t1.getState() == Thread.State.BLOCKED || t2.getState() == Thread.State.BLOCKED) {
                System.out.println("Opposite lock order created circular wait (deadlock detected) as expected.");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        Account resA = new CurrentAccount("ResA", 1000, 0);
        Account resB = new CurrentAccount("ResB", 1000, 0);
        Thread r1 = new Thread(() -> {
            try { resA.transfer(resB, 100); } catch (Exception ignored) {}
        }, "resolved-transfer-1");
        Thread r2 = new Thread(() -> {
            try { resB.transfer(resA, 100); } catch (Exception ignored) {}
        }, "resolved-transfer-2");

        r1.start();
        r2.start();
        try {
            r1.join();
            r2.join();
            System.out.println("Consistent lock order successfully resolved deadlock.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
