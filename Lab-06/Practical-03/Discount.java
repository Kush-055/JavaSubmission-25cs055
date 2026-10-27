import java.util.*;

interface DiscountRule
{
    double apply(double p);
}

class Discount
{
    public static void main(String[] args)
    {
        Scanner sc=new Scanner(System.in);

        DiscountRule d1=(p)->p*0.90;
        DiscountRule d2=(p)->p*0.80;
        DiscountRule d3=(p)->p-100;

        double[] p={500,1000,1500};

        System.out.println("1. 10% discount");
        System.out.println("2. 20% discount");
        System.out.println("3. Rs.100 discount");
        System.out.print("Enter choice: ");

        int ch=sc.nextInt();

        DiscountRule d;

        switch(ch)
        {
            case 1:
                d=d1;
                break;
            case 2:
                d=d2;
                break;
            case 3:
                d=d3;
                break;
            default:
                System.out.println("Wrong choice");
                sc.close();
                return;
        }

        for(int i=0;i<p.length;i++)
        {
            System.out.println("Original: "+p[i]+" Final: "+d.apply(p[i]));
        }

        sc.close();
    }
}