import java.util.*;
import java.io.*;
import java.sql.*;

public class RailwayReservationSystem {
    static Connection conn;
    static Statement stmt;
    static ResultSet rs ;

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/railway_reservation", "root", "password");
            stmt = conn.createStatement();

            Scanner scanner = new Scanner(System.in);

            while (true) {
                System.out.println("1. Add train");
                System.out.println("2. Book seat");
                System.out.println("3. Cancel seat");
                System.out.println("4. View trains");
                System.out.println("5. View bookings");
                System.out.println("6. Exit");

                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        addTrain(scanner);
                        break;
                    case 2:
                        bookSeat(scanner);
                        break;
                    case 3:
                        cancelSeat(scanner);
                        break;
                    case 4:
                        viewTrains();
                        break;
                    case 5:
                        viewBookings();
                        break;
                    case 6:
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void addTrain(Scanner scanner) {
        try {
            System.out.print("Enter train name: ");
            String trainName = scanner.next();

            System.out.print("Enter total seats: ");
            int totalSeats = scanner.nextInt();

            String query = "INSERT INTO trains (train_name, total_seats) VALUES ('" + trainName + "', " + totalSeats + ")";
            stmt.executeUpdate(query);
            System.out.println("Train added successfully.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void bookSeat(Scanner scanner) {
        try {
            System.out.print("Enter train name: ");
            String trainName = scanner.next();

            System.out.print("Enter passenger name: ");
            String passengerName = scanner.next();

            String query = "SELECT * FROM trains WHERE train_name = '" + trainName + "'";
            rs = stmt.executeQuery(query);

            if (rs.next()) {
                int availableSeats = rs.getInt("available_seats");
                if (availableSeats > 0) {
                    availableSeats--;
                    query = "UPDATE trains SET available_seats = " + availableSeats + " WHERE train_name = '" + trainName + "'";
                    stmt.executeUpdate(query);

                    query = "INSERT INTO bookings (train_name, passenger_name, seat_number) VALUES ('" + trainName + "', '" + passengerName + "', " + (rs.getInt("total_seats") - availableSeats) + ")";
                    stmt.executeUpdate(query);
                    System.out.println("Seat booked successfully.");
                } else {
                    System.out.println("Sorry, no seats available.");
                }
            } else {
                System.out.println("Sorry, train not found.");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void cancelSeat(Scanner scanner) {
        try {
            System.out.print("Enter passenger name: ");
            String passengerName = scanner.next();

            String query = "SELECT * FROM bookings WHERE passenger_name = '" + passengerName + "'";
            rs = stmt.executeQuery(query);

            if (rs.next()) {
                String trainName = rs.getString("train_name");
                int seatNumber = rs.getInt("seat_number");

                query = "SELECT * FROM trains WHERE train_name = '" + trainName + "'";
                rs = stmt.executeQuery(query);

                if (rs.next()) {
                    int availableSeats = rs.getInt("available_seats");
                    availableSeats++;
                    query = "UPDATE trains SET available_seats = " + availableSeats + " WHERE train_name = '" + trainName + "'";
                    stmt.executeUpdate(query);

                    query = "DELETE FROM bookings WHERE passenger_name = '" + passengerName + "'";
                    stmt.executeUpdate(query);
                    System.out.println("Seat cancelled successfully.");
                } else {
                    System.out.println("Sorry, train not found.");
                }
            } else {
                System.out.println("Sorry, passenger not found.");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void viewTrains() {
        try {
            String query = "SELECT * FROM trains";
            rs = stmt.executeQuery(query);

            System.out.println("Trains:");
            while (rs.next()) {
                System.out.println("Train Name: " + rs.getString("train_name"));
                System.out.println("Total Seats: " + rs.getInt("total_seats"));
                System.out.println("Available Seats: " + rs.getInt("available_seats"));
                System.out.println();
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void viewBookings() {
        try {
            String query = "SELECT * FROM bookings";
            rs = stmt.executeQuery(query);

            System.out.println("Bookings:");
            while (rs.next()) {
                System.out.println("Train Name: " + rs.getString("train_name"));
                System.out.println("Passenger Name: " + rs.getString("passenger_name"));
                System.out.println("Seat Number: " + rs.getInt("seat_number"));
                System.out.println();
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
