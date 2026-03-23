import java.util.HashMap;
import java.util.Map;

class Room {
    private final int beds;
    private final int sizeSqFt;
    private final double pricePerNight;

    public Room(int beds, int sizeSqFt, double pricePerNight) {
        this.beds = beds;
        this.sizeSqFt = sizeSqFt;
        this.pricePerNight = pricePerNight;
    }

    public int getBeds() {
        return beds;
    }

    public int getSizeSqFt() {
        return sizeSqFt;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }
}

class BookMyStayApp {
    private final Map<String, Room> roomDetails;
    private final Map<String, Integer> availability;

    public BookMyStayApp(Map<String, Room> roomDetails, Map<String, Integer> initialAvailability) {
        this.roomDetails = new HashMap<>(roomDetails);
        this.availability = new HashMap<>(initialAvailability);
    }

    public int getAvailability(String roomType) {
        return availability.getOrDefault(roomType, 0);
    }

    public void updateAvailability(String roomType, int change) {
        availability.put(roomType, availability.getOrDefault(roomType, 0) + change);
    }

    public void displayInventory() {
        System.out.println("Hotel Room Inventory Status\n");

        for (String roomType : roomDetails.keySet()) {
            Room room = roomDetails.get(roomType);
            int availableRooms = getAvailability(roomType);

            System.out.println(roomType + " Room:");
            System.out.println("Beds: " + room.getBeds());
            System.out.println("Size: " + room.getSizeSqFt() + " sqft");
            System.out.println("Price per night: " + room.getPricePerNight());
            System.out.println("Available Rooms: " + availableRooms);
            System.out.println();
        }
    }

    public static void main(String[] args) {
        Map<String, Room> rooms = Map.of(
                "Single", new Room(1, 250, 1500.0),
                "Double", new Room(2, 400, 2500.0),
                "Suite", new Room(3, 750, 5000.0)
        );

        Map<String, Integer> initialAvailability = Map.of(
                "Single", 5,
                "Double", 3,
                "Suite", 2
        );

        BookMyStayApp inventory = new BookMyStayApp(rooms, initialAvailability);
        inventory.displayInventory();
    }
}
