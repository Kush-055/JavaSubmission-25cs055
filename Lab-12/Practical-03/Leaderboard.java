import java.util.*;

class Leaderboard
{
public static void main(String args[])
{
HashMap<String,Integer> m=new HashMap<>();

    m.put("Kush",90);
    m.put("Riya",85);
    m.put("Amit",95);
    m.put("Karan",85);
    m.put("Neha",92);

    ArrayList<Map.Entry<String,Integer>> l=new ArrayList<>(m.entrySet());

    Collections.sort(l,new Comparator<Map.Entry<String,Integer>>()
    {
        public int compare(Map.Entry<String,Integer> x,Map.Entry<String,Integer> y)
        {
            if(y.getValue()!=x.getValue())
                return y.getValue()-x.getValue();

            return x.getKey().compareTo(y.getKey());
        }
    });

    int n=3;

    System.out.println("Top "+n+" players:");

    for(int i=0;i<n && i<l.size();i++)
    {
        System.out.println(l.get(i).getKey()+" : "+l.get(i).getValue());
    }
}

}
