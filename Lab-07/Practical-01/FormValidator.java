import java.lang.annotation.*;
import java.lang.reflect.*;
import java.util.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@interface NotBlank
{
}

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@interface MaxLength
{
    int value();
}

class SignupForm
{
    @NotBlank
    @MaxLength(30)
    String name;

    @NotBlank
    @MaxLength(50)
    String email;

    SignupForm(String n,String e)
    {
        name=n;
        email=e;
    }
}

class FormValidator
{
    static ArrayList<String> check(Object obj)
    {
        ArrayList<String> err=new ArrayList<>();

        Field[] f=obj.getClass().getDeclaredFields();

        for(Field x:f)
        {
            x.setAccessible(true);

            try
            {
                Object v=x.get(obj);

                if(x.isAnnotationPresent(NotBlank.class))
                {
                    if(v==null || v.toString().trim().isEmpty())
                    {
                        err.add(x.getName()+" cannot be blank");
                    }
                }

                if(x.isAnnotationPresent(MaxLength.class))
                {
                    MaxLength m=x.getAnnotation(MaxLength.class);

                    if(v!=null && v.toString().length()>m.value())
                    {
                        err.add(x.getName()+" is too long");
                    }
                }
            }
            catch(Exception e)
            {
                System.out.println("Error");
            }
        }

        return err;
    }
}

class Driver
{
    public static void main(String[] args)
    {
        SignupForm s=new SignupForm("","Kush123456789012345678901234567890@gmail.com");

        ArrayList<String> e=FormValidator.check(s);

        if(e.isEmpty())
        {
            System.out.println("Form is valid");
        }
        else
        {
            System.out.println("Errors:");
            for(String x:e)
            {
                System.out.println(x);
            }
        }
    }
}
