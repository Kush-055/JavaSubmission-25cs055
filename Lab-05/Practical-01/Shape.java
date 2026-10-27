abstract class Shape
{
    abstract double area();
}

class Circle extends Shape
{
    double r;

    Circle(double r)
    {
        this.r=r;
    }

    double area()
    {
        return 3.14*r*r;
    }
}

class Rectangle extends Shape
{
    double l,b;

    Rectangle(double l,double b)
    {
        this.l=l;
        this.b=b;
    }

    double area()
    {
        return l*b;
    }
}

class Triangle extends Shape
{
    double b,h;

    Triangle(double b,double h)
    {
        this.b=b;
        this.h=h;
    }

    double area()
    {
        return 0.5*b*h;
    }
}

class Driver
{
    public static void main(String args[])
    {
        Shape s[]=new Shape[4];

        s[0]=new Circle(5);
        s[1]=new Rectangle(4,6);
        s[2]=new Triangle(8,5);
        s[3]=new Circle(3);

        double total=0;
        double max=0;
        Shape big=null;

        for(int i=0;i<s.length;i++)
        {
            double a=s[i].area();

            System.out.println("Area: "+a);

            total=total+a;

            if(a>max)
            {
                max=a;
                big=s[i];
            }

            System.out.println("Total: "+total);
        }

        System.out.println("Largest area: "+max);
        System.out.println("Shape: "+big.getClass().getSimpleName());
    }
}