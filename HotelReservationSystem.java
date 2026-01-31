import java.io.*;
import java.util.*;

class Room {
    int roomNumber;
    String category; 
    double price;
    boolean isAvailable;

    Room(int roomNumber, String category, double price) {
        this.roomNumber = roomNumber;
        this.category = category;
        this.price = price;
        this.isAvailable = true;
    }
}

class Booking {
    String guestName;
    int roomNumber;
    String checkInDate;
    String checkOutDate;

    Booking(String guestName, int roomNumber, String checkInDate, String checkOutDate) {
        this.guestName = guestName;
        this.roomNumber = roomNumber;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
    }

    @Override
    public String toString() {
        return guestName + "," + roomNumber + "," + checkInDate + "," + checkOutDate;
    }
}

public class HotelReservationSystem {
    static ArrayList<Room> rooms = new ArrayList<>();
    static ArrayList<Booking> bookings = new ArrayList<>();
    static final String BOOKINGS_FILE = "bookings.txt";
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        initializeRooms();
        loadBookings();

        while (true) {
            System.out.println("\n--- Hotel Reservation System ---");
            System.out.println("1. View Available Rooms");
            System.out.println("2. Book a Room");
            System.out.println("3. Cancel Booking");
            System.out.println("4. View My Bookings");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); 

            switch (choice) {
                case 1:
                    viewAvailableRooms();
                    break;
                case 2:
                    bookRoom();
                    break;
                case 3:
                    cancelBooking();
                    break;
                case 4:
                    viewMyBookings();
                    break;
                case 5:
                    saveBookings();
                    System.out.println("Thank you for using the system. Goodbye!");
                    System.exit(0);
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    static void initializeRooms() {
        rooms.add(new Room(101, "Standard", 100));
        rooms.add(new Room(102, "Standard", 100));
        rooms.add(new Room(201, "Deluxe", 200));
        rooms.add(new Room(202, "Deluxe", 200));
        rooms.add(new Room(301, "Suite", 350));
        rooms.add(new Room(302, "Suite", 350));
    }

    static void loadBookings() {
        try (BufferedReader br = new BufferedReader(new FileReader(BOOKINGS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                String guest = data[0];
                int roomNumber = Integer.parseInt(data[1]);
                String checkIn = data[2];
                String checkOut = data[3];
                bookings.add(new Booking(guest, roomNumber, checkIn, checkOut));

                for (Room room : rooms) {
                    if (room.roomNumber == roomNumber) {
                        room.isAvailable = false;
                        break;
                    }
                }
            }
        } catch (IOException e) {
       }
    }

    static void saveBookings() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(BOOKINGS_FILE))) {
            for (Booking b : bookings) {
                bw.write(b.toString());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving bookings: " + e.getMessage());
        }
    }

    static void viewAvailableRooms() {
        System.out.println("\nAvailable Rooms:");
        System.out.printf("%-10s %-10s %-10s\n", "Room No", "Category", "Price");
        for (Room r : rooms) {
            if (r.isAvailable) {
                System.out.printf("%-10d %-10s $%-10.2f\n", r.roomNumber, r.category, r.price);
            }
        }
    }

    static void bookRoom() {
        System.out.print("Enter your name: ");
        String guestName = scanner.nextLine();

        viewAvailableRooms();
        System.out.print("Enter room number to book: ");
        int roomNumber = scanner.nextInt();
        scanner.nextLine();

        Room selectedRoom = null;
        for (Room r : rooms) {
            if (r.roomNumber == roomNumber && r.isAvailable) {
                selectedRoom = r;
                break;
            }
        }
        if (selectedRoom == null) {
            System.out.println("Room not available or invalid number.");
            return;
        }

        System.out.print("Enter check-in date (YYYY-MM-DD): ");
        String checkIn = scanner.nextLine();
        System.out.print("Enter check-out date (YYYY-MM-DD): ");
        String checkOut = scanner.nextLine();

        bookings.add(new Booking(guestName, roomNumber, checkIn, checkOut));
        selectedRoom.isAvailable = false;

        System.out.println("Payment of $" + selectedRoom.price + " successful!");
        System.out.println("Booking confirmed for " + guestName + " in room " + roomNumber);
    }

    static void cancelBooking() {
        System.out.print("Enter your name to cancel booking: ");
        String guestName = scanner.nextLine();

        Booking toRemove = null;
        for (Booking b : bookings) {
            if (b.guestName.equalsIgnoreCase(guestName)) {
                toRemove = b;
                break;
            }
        }

        if (toRemove != null) {
            bookings.remove(toRemove);
            for (Room r : rooms) {
                if (r.roomNumber == toRemove.roomNumber) {
                    r.isAvailable = true;
                    break;
                }
            }
            System.out.println("Booking cancelled successfully.");
        } else {
            System.out.println("No booking found for " + guestName);
        }
    }

    static void viewMyBookings() {
        System.out.print("Enter your name to view bookings: ");
        String guestName = scanner.nextLine();

        boolean found = false;
        for (Booking b : bookings) {
            if (b.guestName.equalsIgnoreCase(guestName)) {
                System.out.println("Room " + b.roomNumber + " | Check-in: " + b.checkInDate + " | Check-out: " + b.checkOutDate);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No bookings found for " + guestName);
        }
    }
}
