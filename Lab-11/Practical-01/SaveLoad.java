import java.io.*;

class Student implements Serializable
{
    String name;
    int roll;
    transient String pass;

    Student(String n,int r,String p)
    {
        name=n;
        roll=r;
        pass=p;
    }

    void show()
    {
        System.out.println("Name: "+name);
        System.out.println("Roll: "+roll);
        System.out.println("Password: "+pass);
    }
}

class SaveLoad
{
    public static void main(String args[]) throws Exception
    {
        Student s[]=new Student[2];

        s[0]=new Student("Kush",55,"abc123");
        s[1]=new Student("Riya",107,"xyz123");

        ObjectOutputStream o=new ObjectOutputStream(new FileOutputStream("data.dat"));
        o.writeObject(s);
        o.close();

        System.out.println("Data saved");

        ObjectInputStream i=new ObjectInputStream(new FileInputStream("data.dat"));
        Student a[]=(Student[])i.readObject();
        i.close();

        System.out.println("\nData after loading:");

        for(int j=0;j<a.length;j++)
        {
            a[j].show();
            System.out.println();
        }
    }
}