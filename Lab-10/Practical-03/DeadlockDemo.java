import java.util.concurrent.CountDownLatch;

class DeadlockDemo
{
    static Object a=new Object();
    static Object b=new Object();

    static CountDownLatch l=new CountDownLatch(2);

    static void bad1()
    {
        synchronized(a)
        {
            System.out.println("T1 locked a");

            l.countDown();

            try
            {
                l.await();
            }
            catch(InterruptedException e)
            {
                Thread.currentThread().interrupt();
                return;
            }

            synchronized(b)
            {
                System.out.println("T1 locked b");
            }
        }
    }

    static void bad2()
    {
        synchronized(b)
        {
            System.out.println("T2 locked b");

            l.countDown();

            try
            {
                l.await();
            }
            catch(InterruptedException e)
            {
                Thread.currentThread().interrupt();
                return;
            }

            synchronized(a)
            {
                System.out.println("T2 locked a");
            }
        }
    }

    static void good(Object x,Object y,String s)
    {
        synchronized(x)
        {
            synchronized(y)
            {
                System.out.println(s+" completed");
            }
        }
    }

    public static void main(String[] args)
    {
        Thread t1=new Thread(()->bad1());
        Thread t2=new Thread(()->bad2());

        t1.setDaemon(true);
        t2.setDaemon(true);

        t1.start();
        t2.start();

        try
        {
            Thread.sleep(500);
        }
        catch(InterruptedException e)
        {
            Thread.currentThread().interrupt();
            return;
        }

        System.out.println("Deadlock demonstrated");

        Object x=new Object();
        Object y=new Object();

        Thread t3=new Thread(()->good(x,y,"T3"));
        Thread t4=new Thread(()->good(x,y,"T4"));

        t3.start();
        t4.start();

        try
        {
            t3.join();
            t4.join();
        }
        catch(InterruptedException e)
        {
            Thread.currentThread().interrupt();
        }

        System.out.println("Deadlock fixed");
    }
}