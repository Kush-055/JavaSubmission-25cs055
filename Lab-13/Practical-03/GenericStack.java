class StackBox<T>
{
Object a[]=new Object[5];
int top=-1;

void push(T x)
{
    if(top==a.length-1)
        System.out.println("Stack is full");
    else
        a[++top]=x;
}

T pop()
{
    if(top==-1)
    {
        System.out.println("Stack is empty");
        return null;
    }

    T x=(T)a[top];
    a[top]=null;
    top--;
    return x;
}

}

class GenericStack
{
public static void main(String args[])
{
StackBox<Integer> s1=new StackBox<>();
s1.push(10);
s1.push(20);

    System.out.println("Integer pop: "+s1.pop());
    System.out.println("Integer pop: "+s1.pop());

    Integer x=s1.pop();
    if(x!=null)
        System.out.println("Integer pop: "+x);

    StackBox<String> s2=new StackBox<>();
    s2.push("Java");
    s2.push("C++");

    System.out.println("String pop: "+s2.pop());
    System.out.println("String pop: "+s2.pop());
}

}
