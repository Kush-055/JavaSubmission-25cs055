import java.lang.annotation.*;
import java.lang.reflect.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@interface Run
{
}

class MyTest
{
    @Run
    void test1()
    {
        System.out.println("Test 1 passed");
    }

    @Run
    void test2()
    {
        System.out.println("Test 2 passed");
    }

    void test3()
    {
        System.out.println("Test 3 passed");
    }

    @Run
    void test4()
    {
        System.out.println("Test 4 passed");
    }
}

class MiniTestRunner
{
    public static void main(String[] args) throws Exception
    {
        MyTest t = new MyTest();
        int c = 0;

        for(Method m : MyTest.class.getDeclaredMethods())
        {
            if(m.isAnnotationPresent(Run.class))
            {
                m.invoke(t);
                c++;
            }
        }

        System.out.println("Total tests run: " + c);
    }
}