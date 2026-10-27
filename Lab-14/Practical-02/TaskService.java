import java.util.ArrayList;

class TaskService
{
ArrayList<Task> a=new ArrayList<>();

void addTask(String name)
{
    if(name==null||name.isEmpty())
        throw new IllegalArgumentException("Invalid task");

    a.add(new Task(name));
}

int getCount()
{
    return a.size();
}

void action(int n)
{
    if(n==1)
        addTask("Java");
    else if(n==2)
        System.out.println("Tasks: "+a);
    else
        throw new IllegalArgumentException("Invalid action");
}

}
