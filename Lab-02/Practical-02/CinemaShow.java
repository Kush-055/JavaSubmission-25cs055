//Practical - 2 / Practice Program - 2 
public class CinemaShow 
{
    private String title;
    private int seatsAvailable;
    private final int capacity;
    private static int totalBooked = 0;

    public CinemaShow(String title, int capacity) {
        this.title = title;
        this.capacity = capacity;
        seatsAvailable = capacity;

    }

    public CinemaShow(String title) {
        this(title, 100);

    }

    public boolean book(int n) {
        if (n <= seatsAvailable) {
            seatsAvailable -= n;
            totalBooked += n;
            return true;

        } else {
            return false;

        }
    }

    public void cancel(int n) {

        seatsAvailable += n;
        if (seatsAvailable > capacity) {
            seatsAvailable = capacity;
        }
    }

    public int getSeatsAvailable() {
        return seatsAvailable;
    }

    public static int getTotalBooked() {
        return totalBooked;
    }

    public static void main(String[] args) {
        CinemaShow s = new CinemaShow("Chal J", 50);
        System.out.println("Book 20 Seats: " + s.book(20));
        System.out.println("Seats Available: " + s.getSeatsAvailable());
        System.out.println("Book 15 Seats : " + s.book(15));
        System.out.println("seats Available : " + s.getSeatsAvailable());
        s.cancel(10);
        System.out.println("After Cancel 10 Seats");
        System.out.println("Seats Available: " + s.getSeatsAvailable());
        System.out.println("Book 30 Seats: " + s.book(30));
        System.out.println("Seats Available: " + s.getSeatsAvailable());
        System.out.println("Total Booked Seats : " + CinemaShow.getTotalBooked());

    }

}
