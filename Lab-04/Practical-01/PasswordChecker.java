
//Practical - 04 / Practice Program - 1
import java.util.Scanner;
 class PasswordChecker 
{
    public static boolean checkLength(String pass)
    {
        return pass.length() >= 8;
    }
   
                public static boolean checkUpper(String pass)
            {
                return pass.matches(".*[A-Z].*");
            }

            public static boolean countDigit(String pw)
            {
                return pw.matches(".*[0-9].*");
            }

            public static boolean checkSpecial(String pass)
            {
                return pass.matches(".*[^A-Za-z0-9].*");
            }
    
    
    public  static String strength(String pw)
    {
        int count = 0;
        if(PasswordChecker.checkLength(pw))
        {
            count++;
        }
        if(PasswordChecker.checkUpper(pw))
        {
            count++;
        }
        if(PasswordChecker.countDigit(pw))
        {
            count++;
        }
        if(PasswordChecker.checkSpecial(pw))
        {
            count++;
        }

        if(count <= 1)
        {
            return "Weak";
        }
        if(count == 2 || count ==3)
        {
            return "Medium";
        }
        if(count == 4)
        {
            return "Strong";
        }
        return "";
        
    }
}
class Driver
{
    public static void main(String[] args)
    {
        
    Scanner sc = new Scanner(System .in);
    String pass;
    System.out.println("Enter Password:");
    pass = sc.next();
    System.out.println("Strength:" + PasswordChecker.strength(pass));
    sc.close();
    }

}
