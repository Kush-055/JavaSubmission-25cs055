//Practical - 03 / practice program - 1
import java.util.Scanner;
class Point
{
    private int x;
    private int y;
  public Point(int x , int y)
    {
        
        this.x = x;
        this.y = y;
        
    }
    @Override
    public String toString()
    {
        return "(" + x + ", " + y + ")";
    }
    @Override
   public boolean equals(Object o)
   {
    if(!(o instanceof Point))
    {
        return false;
    }
    Point p = (Point) o;
    
    return this.x == p.x && this.y == p.y;
   }
   
   


  
   }

    

class Driver
{
     public static void main(String[] args)
   {
    int n;
    Scanner sc = new Scanner(System.in);
    System.out.println("How many point do you want to enter?");
    n = sc.nextInt();
        Point[] p = new Point[n];
        for(int i = 0 ; i < p.length ; i++)
        {
            System.out.println("Enter x and y:");
            
            p[i] = new Point(sc.nextInt(),sc.nextInt());
        }
        int count = 0;
        for(int i = 0 ; i <p.length ; i++)
        {
            for(int j = 0 ; j < i ; j++)
            {
                if(p[i].equals(p[j]))
                {
                    count++;
                    break;

                }
            }
            

        }
        for(int i = 0 ; i < p.length ; i++)
        {
            System.out.println(p[i]);

        }
        System.out.println("" + p.length + "points with " + count +" repeats" );
        System.out.println("distinct: " + (p.length - count));

       
        sc.close();
}
}