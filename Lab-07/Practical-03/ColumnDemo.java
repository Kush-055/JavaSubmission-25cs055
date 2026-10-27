import java.lang.annotation.*;
import java.lang.reflect.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@interface Column
{
    String name();
}

class Student
{
    @Column(name="name")
    String n;

    @Column(name="roll")
    String r;

    @Column(name="course")
    String c;

    public String toString()
    {
        return "Name: " + n + "\nRoll: " + r + "\nCourse: " + c;
    }
}

class ColumnDemo
{
    static void fill(Object obj, String[] h, String[] d) throws Exception
    {
        Class<?> cl = obj.getClass();

        for(Field f : cl.getDeclaredFields())
        {
            if(f.isAnnotationPresent(Column.class))
            {
                Column a = f.getAnnotation(Column.class);
                boolean found = false;

                for(int i = 0; i < h.length; i++)
                {
                    if(h[i].equals(a.name()))
                    {
                        f.setAccessible(true);
                        f.set(obj, d[i]);
                        found = true;
                        break;
                    }
                }

                if(!found)
                {
                    System.out.println("Missing column: " + a.name());
                }
            }
        }
    }

    public static void main(String[] args) throws Exception
    {
        Student s = new Student();

        String[] h = {"name", "roll"};
        String[] d = {"Kush", "55"};

        fill(s, h, d);

        System.out.println(s);
    }
}