import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Hotel {
    private List<Room> rooms;
    private List<Booking> bookings;

    private final String ROOM_FILE = "rooms.dat";
    private final String BOOKING_FILE = "bookings.dat";

    public Hotel() {
        rooms = new ArrayList<>();
        bookings = new ArrayList<>();
        loadRooms();
        loadBookings();
    }

    // Initialize rooms if file is empty
    private void loadRooms() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ROOM_FILE))) {
            rooms = (List<Room>) ois.readObject();
        } catch (Exception e) {
            // Create some default rooms if file not found
            rooms.add(new Room(101, "Standard"));
            rooms.add(new Room(102, "Deluxe"));
            rooms.add(new Room(103, "Suite"));
            rooms.add(new Room(104, "Standard"));
            rooms.add(new Room(105, "Deluxe"));
            saveRooms();
        }
    }

    private void saveRooms() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ROOM_FILE))) {
            oos.writeObject(rooms);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadBookings() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(BOOKING_FILE))) {
            bookings = (List<Booking>) ois.readObject();
        } catch (Exception e) {
            bookings = new ArrayList<>();
        }
    }

    private void saveBookings() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(BOOKING_FILE))) {
            oos.writeObject(bookings);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showAvailableRooms() {
        System.out.println("Available Rooms:");
        for (Room room : rooms) {
            if (room.isAvailable()) {
                System.out.println(room);
            }
        }
    }

    public Room findAvailableRoom(String type) {
        for (Room room : rooms) {
            if (room.isAvailable() && room.getType().equalsIgnoreCase(type)) {
                return room;
            }
        }
        return null;
    }

    public void bookRoom(String customerName, String type) {
        Room room = findAvailableRoom(type);
        if (room != null) {
            room.setAvailable(false);
            Booking booking = new Booking(customerName, room);
            bookings.add(booking);
            saveRooms();
            saveBookings();
            System.out.println("Booking Successful!");
            System.out.println(booking);
        } else {
            System.out.println("No available room of type " + type);
        }
    }

    public void cancelBooking(String customerName) {
        Booking toRemove = null;
        for (Booking b : bookings) {
            if (b.getCustomerName().equalsIgnoreCase(customerName)) {
                b.getRoom().setAvailable(true);
                toRemove = b;
                break;
            }
        }
        if (toRemove != null) {
            bookings.remove(toRemove);
            saveRooms();
            saveBookings();
            System.out.println("Booking cancelled for " + customerName);
        } else {
            System.out.println("No booking found for " + customerName);
        }
    }

    public void showAllBookings() {
        System.out.println("All Bookings:");
        for (Booking b : bookings) {
            System.out.println(b);
        }
    }
}
