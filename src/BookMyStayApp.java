import java.util.LinkedList;
import java.util.Queue;

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

// BookingRequestQueue manages incoming booking requests preserving FIFO order
class BookingRequestQueue {
    private final Queue<Reservation> queue = new LinkedList<>();

    // Accept a booking request from a guest and add it to the queue
    public void submitRequest(Reservation reservation) {
        queue.offer(reservation);
        System.out.println("Booking request submitted: " + reservation);
    }

    // Peek at the next request to process (does not remove)
    public Reservation peekNextRequest() {
        return queue.peek();
    }

    // Retrieve and remove the next request for processing
    public Reservation pollNextRequest() {
        return queue.poll();
    }

    // Check if queue is empty
    public boolean isEmpty() {
        return queue.isEmpty();
    }

    // Returns current queue size
    public int size() {
        return queue.size();
    }
}

// Demo of request intake preserving order
public class BookMyStayApp {
    public static void main(String[] args) {
        BookingRequestQueue requestQueue = new BookingRequestQueue();

        // Simulate multiple guests submitting booking requests
        requestQueue.submitRequest(new Reservation("Alice", "Single"));
        requestQueue.submitRequest(new Reservation("Bob", "Suite"));
        requestQueue.submitRequest(new Reservation("Charlie", "Double"));
        requestQueue.submitRequest(new Reservation("Dana", "Single"));

        System.out.println("\nBooking requests waiting for processing (in order):");
        while (!requestQueue.isEmpty()) {
            Reservation next = requestQueue.pollNextRequest();
            System.out.println(next);
            // No allocation or inventory mutation occurs here, just retrieval for processing
        }
    }
}
