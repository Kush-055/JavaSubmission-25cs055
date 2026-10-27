
class Counter
{
    int c=0;

    void inc()
    {
        c++;
    }

    synchronized void inc2()
    {
        c++;
    }
}

class CounterRace
{
    public static void main(String[] args) throws InterruptedException
    {
        Counter c1=new Counter();
        Counter c2=new Counter();

        Thread[] t1=new Thread[10];
        Thread[] t2=new Thread[10];

        for(int i=0;i<10;i++)
        {
            t1[i]=new Thread(()->{
                for(int j=0;j<1000;j++)
                    c1.inc();
            });

            t2[i]=new Thread(()->{
                for(int j=0;j<1000;j++)
                    c2.inc2();
            });

            t1[i].start();
            t2[i].start();
        }

        for(int i=0;i<10;i++)
        {
            t1[i].join();
            t2[i].join();
        }

        System.out.println("Without synchronization: "+c1.c);
        System.out.println("With synchronization: "+c2.c);
    }
}