import java.util.Scanner;

class App
{
public static void main(String args[])
{
Scanner sc=new Scanner(System.in);
TaskService s=new TaskService();
int n;

    do
    {
        System.out.println("1. Add task");
        System.out.println("2. Show tasks");
        System.out.println("3. Exit");
        System.out.print("Enter choice: ");
        n=sc.nextInt();

        try
        {
            if(n==1)
            {
                s.addTask("Java");
                System.out.println("Task added");
            }
            else if(n==2)
                s.action(2);
            else if(n!=3)
                s.action(n);
        }
        catch(IllegalArgumentException e)
        {
            System.out.println(e.getMessage());
        }

    }while(n!=3);

    System.out.println("Bye");
    sc.close();
}

}
