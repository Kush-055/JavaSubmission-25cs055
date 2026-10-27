//Practical 1/Practice Program-2
//Toll booth 
import java.util.Scanner;
class TollBooth 
{
    record Vehicle(String number, String type){}
    public static void main(String []args)
    {
        Scanner sc = new Scanner(System.in);

        int tolltotal = 0;
        int bike = 0;
        int car = 0;
        int truck = 0;
        
       
        while(true)
        {
             System.out.println("Enter vehicle number(or type done to exit):");
             String number = sc.next().toLowerCase();
             if(number.equals("done"))
             {
                break;
             }
             String type;
     
            
            
                 System.out.println("Enter the type of a vehicle(bike,car,truck):");
                 type = sc.next().toLowerCase();
            
            
            
            Vehicle v = new Vehicle(number, type);

           int toll = switch(v.type())
            {
                case "car" -> 50;
                case "bike" -> 20;
                case "truck" -> 150;
                default ->
               {

               System.out.println("Invalid Type!"); 
                yield 0;
                }  

            };

            tolltotal += toll;
            
            if(v.type().equals("bike"))
            {
                bike++;
            }
           else if(v.type().equals("car"))
            {
                car++;
            }
           else if(v.type().equals("truck"))
            {
                truck++;
            }
           
            
        }
         System.out.println("Total toll:"+tolltotal);
        if(car>=bike && car>=truck)
        {
            System.out.println("Most Frequent:Car");
        }
        else if(bike>=truck)
        {
            System.out.println("Most Frequent:Bike");
        }
        else
        {
            System.out.println("Most Frequent:Truck");
        }
        sc.close();
  

        

    }
    
}
