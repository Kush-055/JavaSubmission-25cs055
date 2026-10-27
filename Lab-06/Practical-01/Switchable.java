/**
 * RemoteControl
 */
public interface Switchable 
{
   
    void on();
    void off();
    boolean isOn();
     String getName();
    default void toggle()
    {
        if(isOn())
        {
            off();
        }
        else
        {
            on();

        }
    }
    
    

    
}
class Fan implements Switchable
{
    boolean state = false;
    public void on()
    {
        state = true;
        System.out.println("Fan is on");
    }
    public  void off()
    {
        state = false;
        System.out.println("Fan is off");
    }
    public  boolean isOn()
    {
        return state;
    }
      public  String getName()
        {
            return "Fan";

        }
}
class Light implements Switchable
{
    boolean state = false;
   public  void on()
    {
        state = true;
        System.out.println("Light is on");
    }
    public  void off()
    {
        state = false;
        System.out.println("Light is off");
    }
    public boolean isOn()
    {
        return state;
    }
       public  String getName()
        {
            return "Light";

        }
}
interface SwitchOnRule
{
    boolean maySwitchOn(Switchable device,int hour);
}
class DayTimeRule implements SwitchOnRule
{
    public boolean maySwitchOn(Switchable device,int hour)
    {
        return hour >= 6 && hour <= 22 ;
    }
}
class Demo
{
    public static void main(String[] args)
    {
        Switchable[] d = { new Light() , new Fan()

        };
        for(int i = 0 ; i < d.length ; i++)
        {
            d[i].toggle();
        }
         for(int i = 0 ; i < d.length ; i++)
        {
            d[i].toggle();
        }
        SwitchOnRule rule = new DayTimeRule();
        if(rule.maySwitchOn(d[0] , 10))
        {
            d[0].on();
        }
        else{
            System.out.println("cannot switch on at this time");
        }
    }

}