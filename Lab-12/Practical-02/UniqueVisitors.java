import java.util.*;

class UniqueVisitors
{
public static void main(String args[])
{
String a[]={"Kush","Riya","Amit","Kush","Karan","Riya","Neha"};

    HashSet<String> h=new HashSet<>();
    LinkedHashSet<String> l=new LinkedHashSet<>();

    for(int i=0;i<a.length;i++)
    {
        h.add(a[i]);
        l.add(a[i]);
    }

    System.out.println("Unique visitors: "+h.size());

    System.out.println("First three visitors:");

    int c=0;

    for(String s:l)
    {
        if(c<3)
        {
            System.out.println(s);
            c++;
        }
    }
}

}
