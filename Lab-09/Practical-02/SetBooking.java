class Seat
{
    int left=5;
    int booked=0;

    void book()
    {
        if(left>0)
        {
            try
            {
                Thread.sleep(10);
            }
            catch(InterruptedException e)
            {
            }

            left--;
            booked++;
        }
    }

    synchronized void book2()
    {
        if(left>0)
        {
            try
            {
                Thread.sleep(10);
            }
            catch(InterruptedException e)
            {
            }

            left--;
            booked++;
        }
    }
}

class SeatBooking
{
    public static void main(String[] args) throws InterruptedException
    {
        Seat s1=new Seat();
        Seat s2=new Seat();

        Thread[] t1=new Thread[10];
        Thread[] t2=new Thread[10];

        for(int i=0;i<10;i++)
        {
            t1[i]=new Thread(()->s1.book());
            t2[i]=new Thread(()->s2.book2());

            t1[i].start();
            t2[i].start();
        }

        for(int i=0;i<10;i++)
        {
            t1[i].join();
            t2[i].join();
        }

        System.out.println("Without synchronization:");
        System.out.println("Seats booked: "+s1.booked);
        System.out.println("Seats left: "+s1.left);

        System.out.println("With synchronization:");
        System.out.println("Seats booked: "+s2.booked);
        System.out.println("Seats left: "+s2.left);
    }
}