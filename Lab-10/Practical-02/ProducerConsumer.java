class Buffer
{
    int item;
    boolean full=false;

    synchronized void produce(int n) throws InterruptedException
    {
        while(full)
            wait();

        item=n;
        full=true;

        System.out.println("Produced: "+item);

        notify();
    }

    synchronized void consume() throws InterruptedException
    {
        while(!full)
            wait();

        System.out.println("Consumed: "+item);

        full=false;

        notify();
    }
}

class ProducerConsumer
{
    public static void main(String[] args)
    {
        Buffer b=new Buffer();

        Thread p=new Thread(()->{
            try
            {
                for(int i=1;i<=5;i++)
                    b.produce(i);
            }
            catch(InterruptedException e)
            {
                Thread.currentThread().interrupt();
            }
        });

        Thread c=new Thread(()->{
            try
            {
                for(int i=1;i<=5;i++)
                    b.consume();
            }
            catch(InterruptedException e)
            {
                Thread.currentThread().interrupt();
            }
        });

        p.start();
        c.start();

        try
        {
            p.join();
            c.join();
        }
        catch(InterruptedException e)
        {
            Thread.currentThread().interrupt();
        }

        System.out.println("Done");
    }
}