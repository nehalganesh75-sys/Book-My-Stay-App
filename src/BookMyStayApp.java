import java.util.*;

// Represents a guest's booking intent
class Reservation {
    private final String guestName;
    private final String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "guest='" + guestName + '\'' +
                ", roomType='" + roomType + '\'' +
                '}';
    }
}

// Inventory service manages room availability
class InventoryService {
    private final Map<String, Integer> availability;

    public InventoryService(Map<String, Integer> initialAvailability) {
        this.availability = new HashMap<>(initialAvailability);
    }

    // Check if room type has availability
    public boolean hasAvailability(String roomType) {
        return availability.getOrDefault(roomType, 0) > 0;
    }

    // Decrement availability for allocated room type atomically
    public boolean allocateRoom(String roomType) {
        int count = availability.getOrDefault(roomType, 0);
        if (count > 0) {
            availability.put(roomType, count - 1);
            return true;
        }
        return false;
    }

    public int getAvailability(String roomType) {
        return availability.getOrDefault(roomType, 0);
    }
}

// BookingRequestQueue handles incoming requests in FIFO order
class BookingRequestQueue {
    private final Queue<Reservation> queue = new LinkedList<>();

    public void submitRequest(Reservation reservation) {
        queue.offer(reservation);
    }

    public Reservation pollNextRequest() {
        return queue.poll();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}

// BookingService processes requests, assigns unique room IDs, and updates inventory
class BookingService {
    private final BookingRequestQueue requestQueue;
    private final InventoryService inventoryService;
    // Map roomType -> Set of allocated room IDs
    private final Map<String, Set<String>> allocatedRooms = new HashMap<>();
    private int nextRoomId = 1;  // For unique room ID generation

    public BookingService(BookingRequestQueue requestQueue, InventoryService inventoryService) {
        this.requestQueue = requestQueue;
        this.inventoryService = inventoryService;
    }

    // Process one booking request from the queue, returns confirmation or null if no allocation possible
    public String processNextBooking() {
        if (requestQueue.isEmpty()) return null;

        Reservation reservation = requestQueue.pollNextRequest();
        String roomType = reservation.getRoomType();

        if (!inventoryService.hasAvailability(roomType)) {
            // Cannot allocate, room type unavailable
            System.out.println("No availability for " + roomType + ". Reservation for " + reservation.getGuestName() + " cannot be confirmed.");
            return null;
        }

        // Allocate room atomically: update inventory and assign unique room ID
        boolean allocated = inventoryService.allocateRoom(roomType);
        if (!allocated) {
            // Race condition fallback - double check failed
            System.out.println("Allocation failed due to race condition for " + roomType + ". Reservation for " + reservation.getGuestName() + " cannot be confirmed.");
            return null;
        }

        String roomId = generateUniqueRoomId();

        // Record assigned room ID per room type
        allocatedRooms.putIfAbsent(roomType, new HashSet<>());
        Set<String> assignedRooms = allocatedRooms.get(roomType);
        // Defensive uniqueness check (should never fail due to controlled ID generation)
        if (assignedRooms.contains(roomId)) {
            throw new IllegalStateException("Room ID duplication detected: " + roomId);
        }
        assignedRooms.add(roomId);

        String confirmation = String.format("Reservation confirmed for %s: %s Room assigned (Room ID: %s)",
                reservation.getGuestName(), roomType, roomId);
        System.out.println(confirmation);
        return confirmation;
    }

    private String generateUniqueRoomId() {
        // Simple unique room ID generator: R1, R2, R3, ...
        return "R" + (nextRoomId++);
    }

    // For debugging or reporting - shows all allocated rooms
    public void printAllocatedRooms() {
        System.out.println("\nAllocated Rooms:");
        allocatedRooms.forEach((roomType, ids) -> {
            System.out.println(roomType + ": " + ids);
        });
    }
}

// Demo with queue intake and booking confirmation
public class BookMyStayApp {
    public static void main(String[] args) {
        // Initialize inventory with counts
        Map<String, Integer> initialAvailability = Map.of(
                "Single", 2,
                "Double", 1,
                "Suite", 1
        );

        InventoryService inventory = new InventoryService(initialAvailability);
        BookingRequestQueue requestQueue = new BookingRequestQueue();

        // Simulate incoming booking requests
        requestQueue.submitRequest(new Reservation("Alice", "Single"));
        requestQueue.submitRequest(new Reservation("Bob", "Suite"));
        requestQueue.submitRequest(new Reservation("Charlie", "Double"));
        requestQueue.submitRequest(new Reservation("Dana", "Single"));
        requestQueue.submitRequest(new Reservation("Eve", "Suite")); // Exceeds availability

        BookingService bookingService = new BookingService(requestQueue, inventory);

        // Process all booking requests in FIFO order
        while (!requestQueue.isEmpty()) {
            bookingService.processNextBooking();
        }

        bookingService.printAllocatedRooms();
    }
}
