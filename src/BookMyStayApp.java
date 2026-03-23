// Abstract class
abstract class Room {
    protected int beds;
    protected double price;

    public Room(int beds, double price) {
        this.beds = beds;
        this.price = price;
    }

    // Abstract method
    public abstract String getType();

    public void displayDetails(int availability) {
        System.out.println("Room Type: " + getType());
        System.out.println("Beds: " + beds);
        System.out.println("Price: $" + price);
        System.out.println("Available: " + availability);
        System.out.println("----------------------");
    }
}

// Concrete classes
class SingleRoom extends Room {
    public SingleRoom() {
        super(1, 50);
    }

    public String getType() {
        return "Single Room";
    }
}

class DoubleRoom extends Room {
    public DoubleRoom() {
        super(2, 90);
    }

    public String getType() {
        return "Double Room";
    }
}

class SuiteRoom extends Room {
    public SuiteRoom() {
        super(3, 150);
    }

    public String getType() {
        return "Suite Room";
    }
}

// Main application
public class BookMyStayApp {
    public static void main(String[] args) {
        // Create room objects (polymorphism)
        Room single = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suite = new SuiteRoom();

        // Static availability variables
        int singleAvailable = 5;
        int doubleAvailable = 3;
        int suiteAvailable = 2;

        // Display details
        single.displayDetails(singleAvailable);
        doubleRoom.displayDetails(doubleAvailable);
        suite.displayDetails(suiteAvailable);
    }
}
