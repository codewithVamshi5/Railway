import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

class Seat {
    String seatNumber;
    boolean isBooked;
    String gender;

    Seat(String seatNumber, String gender) {
        this.seatNumber = seatNumber;
        this.isBooked = false;
        this.gender = gender;
    }
}

class Train {
    String trainID;
    String trainName;
    String source;
    String destination;
    List<Seat> seats;
    double ticketPrice;

    Train(String trainID, String trainName, String source, String destination, double ticketPrice) {
        this.trainID = trainID;
        this.trainName = trainName;
        this.source = source;
        this.destination = destination;
        this.seats = new ArrayList<>();
        this.ticketPrice = ticketPrice;
    }

    void addSeats(int totalSeats) {
        for (int i = 1; i <= totalSeats; i++) {
            String gender = (i % 2 == 0) ? "female" : "male";
            seats.add(new Seat("S" + i, gender));
        }
    }

    List<Seat> getAvailableSeats(String gender) {
        List<Seat> availableSeats = new ArrayList<>();
        for (Seat seat : seats) {
            if (!seat.isBooked && seat.gender.equals(gender)) {
                availableSeats.add(seat);
            }
        }
        return availableSeats;
    }

    List<Seat> getBookedSeats() {
        List<Seat> bookedSeats = new ArrayList<>();
        for (Seat seat : seats) {
            if (seat.isBooked) {
                bookedSeats.add(seat);
            }
        }
        return bookedSeats;
    }

    void bookSeat(String seatNumber) {
        for (Seat seat : seats) {
            if (seat.seatNumber.equals(seatNumber) && !seat.isBooked) {
                seat.isBooked = true;
            }
        }
    }

    void cancelBooking(String seatNumber) {
        for (Seat seat : seats) {
            if (seat.seatNumber.equals(seatNumber) && seat.isBooked) {
                seat.isBooked = false;
            }
        }
    }
}

class Customer {
    String name;
    int age;
    String gender;
    String contactDetails;
    double totalPayment;

    Customer(String name, int age, String gender, String contactDetails) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.contactDetails = contactDetails;
        this.totalPayment = 0;
    }

    void makePayment(double amount) {
        this.totalPayment += amount;
        JOptionPane.showMessageDialog(null, "Payment successful. Total Payment: ₹" + amount);
    }

    void refundPayment(double amount) {
        if (this.totalPayment >= amount) {
            this.totalPayment -= amount;
            JOptionPane.showMessageDialog(null, "Refund successful. Refunded: ₹" + amount);
        } else {
            JOptionPane.showMessageDialog(null, "Refund amount exceeds payment made.");
        }
    }
}

class RailwayReservationSystem {
    List<Train> trains;

    RailwayReservationSystem() {
        trains = new ArrayList<>();
    }

    void addTrain(String trainID, String trainName, String source, String destination, double ticketPrice, int totalSeats) {
        Train train = new Train(trainID, trainName, source, destination, ticketPrice);
        train.addSeats(totalSeats);
        trains.add(train);
    }

    void displayAvailableTrains() {
        StringBuilder trainList = new StringBuilder("Available Trains:\n");
        for (Train train : trains) {
            trainList.append("Train ID: " + train.trainID + " | " + train.trainName + " | Source: " + train.source + " | Destination: " + train.destination + " | Ticket Price: ₹" + train.ticketPrice + "\n");
        }
        JOptionPane.showMessageDialog(null, trainList.toString());
    }

    Train findTrain(String trainID) {
        for (Train train : trains) {
            if (train.trainID.equals(trainID)) {
                return train;
            }
        }
        return null;
    }

    List<Seat> getAvailableSeatsForGender(String trainID, String gender) {
        Train train = findTrain(trainID);
        if (train != null) {
            return train.getAvailableSeats(gender);
        }
        return new ArrayList<>();
    }

    List<Seat> getBookedSeats(String trainID) {
        Train train = findTrain(trainID);
        if (train != null) {
            return train.getBookedSeats();
        }
        return new ArrayList<>();
    }

    void bookTicket(String trainID, String seatNumber, Customer customer, boolean foodOption) {
        Train train = findTrain(trainID);
        if (train != null) {
            double totalCost = train.ticketPrice;
            if (foodOption) {
                totalCost += 500.0;  // Add food charge of ₹500
                JOptionPane.showMessageDialog(null, "Food option selected. Additional charge: ₹500");
            }
            customer.makePayment(totalCost);
            train.bookSeat(seatNumber);
            JOptionPane.showMessageDialog(null, "Ticket booked successfully for " + customer.name + " on Seat " + seatNumber);
        } else {
            JOptionPane.showMessageDialog(null, "Train not found!");
        }
    }

    void cancelTicket(String trainID, String seatNumber, Customer customer) {
        Train train = findTrain(trainID);
        if (train != null) {
            boolean seatBooked = false;
            for (Seat seat : train.seats) {
                if (seat.seatNumber.equals(seatNumber) && seat.isBooked) {
                    seatBooked = true;
                    break;
                }
            }

            if (seatBooked) {
                train.cancelBooking(seatNumber);
                double refundAmount = train.ticketPrice;
                customer.refundPayment(refundAmount);
                JOptionPane.showMessageDialog(null, "Ticket for Seat " + seatNumber + " has been cancelled.");
            } else {
                JOptionPane.showMessageDialog(null, "Seat " + seatNumber + " is not booked, cancellation not possible.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Train not found!");
        }
    }

    void displaySeats(String trainID) {
        Train train = findTrain(trainID);
        if (train != null) {
            StringBuilder seatDisplay = new StringBuilder("Seats for Train " + train.trainName + ":\n");
            for (Seat seat : train.seats) {
                seatDisplay.append("Seat " + seat.seatNumber + " (" + seat.gender + ") - " + (seat.isBooked ? "Booked" : "Available") + "\n");
            }
            JOptionPane.showMessageDialog(null, seatDisplay.toString());
        } else {
            JOptionPane.showMessageDialog(null, "Train not found!");
        }
    }

    void displayBookedSeats(String trainID) {
        Train train = findTrain(trainID);
        if (train != null) {
            List<Seat> bookedSeats = train.getBookedSeats();
            if (bookedSeats.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No seats are booked for this train.");
            } else {
                StringBuilder bookedSeatDisplay = new StringBuilder("Booked Seats for Train " + train.trainName + ":\n");
                for (Seat seat : bookedSeats) {
                    bookedSeatDisplay.append("Seat " + seat.seatNumber + " (" + seat.gender + ")\n");
                }
                JOptionPane.showMessageDialog(null, bookedSeatDisplay.toString());
            }
        } else {
            JOptionPane.showMessageDialog(null, "Train not found!");
        }
    }
}

public class Main {
    public static void main(String[] args) {
        RailwayReservationSystem system = new RailwayReservationSystem();
        system.addTrain("T001", "Express 101", "Hyderabad", "Delhi", 1000.0, 10);
        system.addTrain("T002", "Superfast 202", "Mumbai", "Patna", 1200.0, 8);
        system.addTrain("T003", "Shatabdi 303", "Delhi", "Mumbai", 1500.0, 12);
        system.addTrain("T004", "Rajdhani 404", "Kolkata", "Bangalore", 1800.0, 15);
        system.addTrain("T005", "Duronto 505", "Chennai", "Hyderabad", 900.0, 8);

        system.displayAvailableTrains();  // Show all available trains

        String trainID = JOptionPane.showInputDialog("Enter Train ID to select (e.g., T001):");
        String gender = JOptionPane.showInputDialog("Enter gender for seat preference (male/female):");

        List<Seat> availableSeats = system.getAvailableSeatsForGender(trainID, gender);
        if (availableSeats.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No available seats for " + gender);
        } else {
            StringBuilder availableSeatsList = new StringBuilder("Available Seats for " + gender + ":\n");
            for (Seat seat : availableSeats) {
                availableSeatsList.append(seat.seatNumber + "\n");
            }
            JOptionPane.showMessageDialog(null, availableSeatsList.toString());

            String seatNumber = JOptionPane.showInputDialog("Enter Seat Number to book (e.g., S1):");
            String name = JOptionPane.showInputDialog("Enter your name:");
            int age = Integer.parseInt(JOptionPane.showInputDialog("Enter your age:"));
            String contactDetails = JOptionPane.showInputDialog("Enter your contact details:");

            Customer customer = new Customer(name, age, gender, contactDetails);
            boolean foodOption = JOptionPane.showConfirmDialog(null, "Do you want to add food?") == JOptionPane.YES_OPTION;

            system.bookTicket(trainID, seatNumber, customer, foodOption);

            String cancelOption = JOptionPane.showInputDialog("Do you want to cancel a ticket? (yes/no):");
            if (cancelOption.equalsIgnoreCase("yes")) {
                String cancelSeatNumber = JOptionPane.showInputDialog("Enter Seat Number to cancel (e.g., S1):");
                system.cancelTicket(trainID, cancelSeatNumber, customer);
            }

            String viewBookedSeatsOption = JOptionPane.showInputDialog("Do you want to view the booked seats? (yes/no):");
            if (viewBookedSeatsOption.equalsIgnoreCase("yes")) {
                system.displayBookedSeats(trainID);
            }
        }
    }
}
