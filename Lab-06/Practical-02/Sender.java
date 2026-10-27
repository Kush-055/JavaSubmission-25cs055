interface Notifier
{
    void send(String s);
}

interface Urgent
{
}

class Sender

{
    public static void main(String[] args)
    {
        Notifier e=(s)->System.out.println("Email: "+s);
        Notifier m=(s)->System.out.println("SMS: "+s);

        Notifier[] n={e,m};

        String s="College event tomorrow";

        for(int i=0;i<n.length;i++)
        {
            n[i].send(s);
        }

        Notifier u=(s)->System.out.println("Urgent Email: "+s);

        class UrgentEmail implements Notifier,Urgent
        {
            public void send(String s)
            {
                System.out.println("Urgent Email: "+s);
            }
        }

        Notifier x=new UrgentEmail();

        System.out.println("Urgent message:");

        x.send(s);
        if(x instanceof Urgent)
            x.send(s);
    }
}