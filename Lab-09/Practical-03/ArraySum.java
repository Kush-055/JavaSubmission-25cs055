class ArraySum
{
    static int sum=0;

    public static void main(String[] args) throws InterruptedException
    {
        int[] a=new int[1000];

        for(int i=0;i<a.length;i++)
            a[i]=1;

        Thread[] t=new Thread[10];

        sum=0;

        for(int i=0;i<10;i++)
        {
            int st=i*100;
            int en=st+100;

            t[i]=new Thread(()->{
                for(int j=st;j<en;j++)
                {
                    sum+=a[j];
                }
            });

            t[i].start();
        }

        for(int i=0;i<10;i++)
            t[i].join();

        System.out.println("Without synchronization: "+sum);

        sum=0;

        for(int i=0;i<10;i++)
        {
            int st=i*100;
            int en=st+100;

            t[i]=new Thread(()->{
                for(int j=st;j<en;j++)
                {
                    synchronized(ArraySum.class)
                    {
                        sum+=a[j];
                    }
                }
            });

            t[i].start();
        }

        for(int i=0;i<10;i++)
            t[i].join();

        System.out.println("With synchronization: "+sum);

        int[] part=new int[10];

        for(int i=0;i<10;i++)
        {
            int st=i*100;
            int en=st+100;
            int k=i;

            t[i]=new Thread(()->{
                for(int j=st;j<en;j++)
                    part[k]+=a[j];
            });

            t[i].start();
        }

        for(int i=0;i<10;i++)
            t[i].join();

        int total=0;

        for(int i=0;i<10;i++)
            total+=part[i];

        System.out.println("Using partial sums: "+total);
    }
}