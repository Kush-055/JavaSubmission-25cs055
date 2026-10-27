class MyResource implements AutoCloseable
{
    MyResource()
    {
        System.out.println("Resource opened");
    }

    public void close()
    {
        System.out.println("Resource closed");
    }
}

class ResourceDemo
{
    public static void main(String[] args)
    {
        try(MyResource r=new MyResource())
        {
            System.out.println("Inside try block");
            throw new RuntimeException("Original error");
        }
        catch(Exception e)
        {
            System.out.println("Error: "+e.getMessage());
        }
    }
}