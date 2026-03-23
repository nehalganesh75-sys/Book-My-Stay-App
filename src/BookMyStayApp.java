import java.util.HashMap;
import java.util.Map;

// Domain model for room details
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

// Inventory managing availability state (mutable)
class RoomInventory {
    private final Map<String, Integer> availability;

    public RoomInventory(Map<String, Integer> initialAvailability) {
        this.availability = new HashMap<>(initialAvailability);
    }

    public int getAvailability(String roomType) {
        return availability.getOrDefault(roomType, 0);
    }

    public void updateAvailability(String roomType, int change) {
        availability.put(roomType, availability.getOrDefault(roomType, 0) + change);
    }
}

// Search service: read-only access to room info and availability
class SearchService {
    private final Map<String, Room> roomDetails;
    private final RoomInventory inventory;

    public SearchService(Map<String, Room> roomDetails, RoomInventory inventory) {
        this.roomDetails = roomDetails;
        this.inventory = inventory;
    }

    // Displays only available rooms with their details — no state modification
    public void displayAvailableRooms() {
        System.out.println("Available Rooms:\n");
        for (String roomType : roomDetails.keySet()) {
            int available = inventory.getAvailability(roomType);
            if (available > 0) {  // filter out unavailable room types
                Room room = roomDetails.get(roomType);
                System.out.println(roomType + " Room:");
                System.out.println("Beds: " + room.getBeds());
                System.out.println("Size: " + room.getSizeSqFt() + " sqft");
                System.out.println("Price per night: " + room.getPricePerNight());
                System.out.println("Available Rooms: " + available);
                System.out.println();
            }
        }
    }
}

// Demo of guest initiating a search
public class BookMyStayApp {
    public static void main(String[] args) {
        Map<String, Room> rooms = Map.of(
                "Single", new Room(1, 250, 1500.0),
                "Double", new Room(2, 400, 2500.0),
                "Suite", new Room(3, 750, 5000.0)
        );

        Map<String, Integer> initialAvailability = Map.of(
                "Single", 5,
                "Double", 3,
                "Suite", 0 // Suite is unavailable
        );

        RoomInventory inventory = new RoomInventory(initialAvailability);
        SearchService searchService = new SearchService(rooms, inventory);

        // Guest triggers a search for available rooms
        searchService.displayAvailableRooms();
    }
}
