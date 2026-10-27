abstract class Media
{
    String name;

    Media(String n)
    {
        name=n;
    }

    abstract int fee(int d);
}

class Book extends Media
{
    Book(String n)
    {
        super(n);
    }

    int fee(int d)
    {
        return d*2;
    }
}

class DVD extends Media
{
    DVD(String n)
    {
        super(n);
    }

    int fee(int d)
    {
        return d*5;
    }
}

class Magazine extends Media
{
    Magazine(String n)
    {
        super(n);
    }

    int fee(int d)
    {
        return d;
    }
}

class Driver
{
    public static void main(String[] args)
    {
        Media[] m={
            new Book("Java Book"),
            new DVD("Movie DVD"),
            new Magazine("Tech Magazine")
        };

        int[] d={3,2,5};
        int t=0;

        for(int i=0;i<m.length;i++)
        {
            int f=m[i].fee(d[i]);
            System.out.println(m[i].name+" fee: "+f);
            t=t+f;
        }

        System.out.println("Total fee: "+t);
    }
}