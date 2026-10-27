import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class ThreadPoolRunner
{
    public static void main(String[] args) throws InterruptedException
    {
        ExecutorService ex=Executors.newFixedThreadPool(3);

        for(int i=1;i<=10;i++)
        {
            int id=i;

            ex.execute(()->{
                System.out.println("Task "+id+" running on "+Thread.currentThread().getName());

                try
                {
                    Thread.sleep(500);
                }
                catch(InterruptedException e)
                {
                    System.out.println("Task interrupted");
                    Thread.currentThread().interrupt();
                }
            });
        }

        ex.shutdown();
        ex.awaitTermination(1,TimeUnit.MINUTES);

        System.out.println("All tasks completed");
    }
}