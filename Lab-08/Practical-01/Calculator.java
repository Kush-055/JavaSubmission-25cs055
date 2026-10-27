import java.util.Scanner;

class DivideByZeroException extends Exception
{
    DivideByZeroException(String s)
    {
        super(s);
    }
}

class Calculator
{
    public static void main(String[] args)
    {
        Scanner sc=new Scanner(System.in);
        boolean ok=false;

        while(!ok)
        {
            try
            {
                System.out.print("Enter first number: ");
                double a=Double.parseDouble(sc.next());

                System.out.print("Enter operator (+,-,*,/): ");
                String op=sc.next();

                System.out.print("Enter second number: ");
                double b=Double.parseDouble(sc.next());

                double ans=0;

                switch(op)
                {
                    case "+":
                        ans=a+b;
                        break;
                    case "-":
                        ans=a-b;
                        break;
                    case "*":
                        ans=a*b;
                        break;
                    case "/":
                        if(b==0)
                            throw new DivideByZeroException("Cannot divide by zero");
                        ans=a/b;
                        break;
                    default:
                        System.out.println("Wrong operator");
                        continue;
                }

                System.out.println("Answer: "+ans);
                ok=true;
            }
            catch(DivideByZeroException e)
            {
                System.out.println(e.getMessage());
            }
            catch(NumberFormatException e)
            {
                System.out.println("Enter valid numbers");
            }
            finally
            {
                System.out.println("Calculation attempt finished");
            }
        }

        sc.close();
    }
}