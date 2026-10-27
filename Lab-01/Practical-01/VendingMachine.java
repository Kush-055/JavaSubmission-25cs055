//Practical 1
// Practice Program -1
//Vending machine 
import java.util.Scanner;

class VendingMachine
{
    public static void main(String[] args)
    {

        enum Coin{ONE , TWO , FIVE , TEN};
        
        int PRICE = 15;
        int total = 0;
        System.out.println("Enter coins(ONE , TWO , FIVE , TEN):");
        Scanner sc = new Scanner(System.in);
        Coin c;
        
        while(total<PRICE)
        {
            String input = sc.next().toUpperCase();
            try
            {
                c = Coin.valueOf(input);
                
            }
            catch(Exception error)
            {
                System.out.println("Invalid coin");
                continue;
            }
            int value=0;
            switch(c)
            {
                case ONE -> value = 1;
                case TWO -> value = 2;
                case FIVE -> value = 5;
                case TEN -> value = 10;
            }
            total += value;
            System.out.println("So far:"+total);



        }
        System.out.println("Paid: "+total + " Change:"+(total- PRICE));
        sc.close();

    }


}