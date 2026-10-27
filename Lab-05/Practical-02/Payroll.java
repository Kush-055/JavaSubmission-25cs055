abstract class Employee
{
    String name;
    int id;

    Employee(String name,int id)
    {
        this.name=name;
        this.id=id;
    }

    abstract double monthlySalary();
}

class FullTime extends Employee
{
    double salary;

    FullTime(String name,int id,double salary)
    {
        super(name,id);
        this.salary=salary;
    }

    double monthlySalary()
    {
        return salary;
    }
}

class PartTime extends Employee
{
    int hours;
    double rate;

    PartTime(String name,int id,int hours,double rate)
    {
        super(name,id);
        this.hours=hours;
        this.rate=rate;
    }

    double monthlySalary()
    {
        return hours*rate;
    }
}

class Intern extends Employee
{
    double stipend;

    Intern(String name,int id,double stipend)
    {
        super(name,id);
        this.stipend=stipend;
    }

    double monthlySalary()
    {
        return stipend;
    }
}

class Driver
{
    public static void main(String args[])
    {
        Employee e[]=new Employee[3];

        e[0]=new FullTime("Riya",101,30000);
        e[1]=new PartTime("Amit",102,80,200);
        e[2]=new Intern("Neha",103,8000);

        double total=0;

        for(int i=0;i<e.length;i++)
        {
            double s=e[i].monthlySalary();

            System.out.println("Name: "+e[i].name);
            System.out.println("ID: "+e[i].id);
            System.out.println("Salary: "+s);

            total=total+s;

            if(e[i] instanceof Intern)
            {
                System.out.println("Intern - stipend paid");
            }

            System.out.println();
        }

        System.out.println("Total salary: "+total);
    }
}