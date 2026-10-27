class Box<T>
{
T a;

void set(T a)
{
    this.a=a;
}

T get()
{
    return a;
}

}

class Pair<A,B>
{
A a;
B b;

Pair(A a,B b)
{
    this.a=a;
    this.b=b;
}

void show()
{
    System.out.println(a+" "+b);
}

}

class GenericContainers
{
static <T> T first(T a[])
{
return a[0];
}

public static void main(String args[])
{
    Box<Integer> b1=new Box<>();
    b1.set(100);
    System.out.println("Box: "+b1.get());

    Box<String> b2=new Box<>();
    b2.set("Java");
    System.out.println("Box: "+b2.get());

    Pair<String,Integer> p1=new Pair<>("Kush",20);
    p1.show();

    Integer a[]={10,20,30};
    String s[]={"Java","C++","Python"};

    System.out.println("First number: "+first(a));
    System.out.println("First word: "+first(s));
}

}
