import java.util.*;

class WordFrequency
{
public static void main(String args[])
{
String s="java is easy and java is useful";
String a[]=s.split(" ");

    HashMap<String,Integer> m=new HashMap<>();

    for(int i=0;i<a.length;i++)
    {
        if(m.containsKey(a[i]))
            m.put(a[i],m.get(a[i])+1);
        else
            m.put(a[i],1);
    }

    ArrayList<Map.Entry<String,Integer>> l=new ArrayList<>(m.entrySet());

    Collections.sort(l,new Comparator<Map.Entry<String,Integer>>()
    {
        public int compare(Map.Entry<String,Integer> x,Map.Entry<String,Integer> y)
        {
            return y.getValue()-x.getValue();
        }
    });

    System.out.println("Word frequency:");

    for(Map.Entry<String,Integer> e:l)
    {
        System.out.println(e.getKey()+" : "+e.getValue());
    }
}

}
