//Practical 2/Practice Program - 1
class Thermostat {
    private String location;
    private int temperature;
    private static final int MIN = 16;
    private static final int MAX = 30;
    private static int activeCount = 0;

    
    public Thermostat(String location, int startTemp) {
        // super()
        this.location = location;
        if (startTemp > MAX || startTemp < MIN) {
            temperature = 22;

        } else {
            temperature = startTemp;

        }
        activeCount++;
    }

    public Thermostat(String location) {
        this(location, 22);

    }

    public void raise() {
        if (temperature < MAX) {
            temperature++;
        } else {
            System.out.println("already at maximum (30)");

        }
    }

    public void lower() {
        if (temperature > MIN) {
            temperature--;
        } else {
            System.out.println("Already at minimum (16)");

        }
    }

    public int getTemperature() {
        return temperature;
    }

    public static int getActiveCount() {
        return activeCount;
    }

}

class ThermostatDemo {
    public static void main(String[] args) {
        Thermostat t1 = new Thermostat("515");
        Thermostat t2 = new Thermostat("612", 32);
        for (int i = 0; i < 10; i++) {
            t1.raise();
            System.out.println("Temperature rises to " + t1.getTemperature());
        }
        for (int i = 0; i < 20; i++) {
            t2.lower();
            System.out.println("Temperature falls to  " + t2.getTemperature());
        }
        System.out.println("count " + Thermostat.getActiveCount());

    }
}
