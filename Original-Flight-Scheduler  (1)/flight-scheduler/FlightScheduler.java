import java.util.Scanner;

// Original classroom project. All flight details are fictional.
class Flight {
    // TOPICS: variables, data types, and objects
    String number;
    String destination;
    int departureMinutes;
    int delayMinutes;
    int gate;
    boolean cancelled;

    Flight(String number, String destination, int departureMinutes, int gate) {
        this.number = number;
        this.destination = destination;
        this.departureMinutes = departureMinutes;
        this.gate = gate;
        this.delayMinutes = 0;
        this.cancelled = false;
    }

    // TOPICS: methods, expressions, and conditional statements
    String status() {
        if (cancelled) {
            return "Cancelled";
        } else if (delayMinutes > 0) {
            return "Delayed";
        } else {
            return "On time";
        }
    }
}

public class FlightScheduler {
    // TOPIC: arrays of objects. Capacity is fixed at ten flights.
    static Flight[] flights = new Flight[10];
    static int flightCount = 0;
    static Scanner input = new Scanner(System.in);

    public static void main(String[] args) {
        flights[flightCount++] = new Flight("SK101", "Delhi", 540, 1);
        flights[flightCount++] = new Flight("SK202", "Mumbai", 630, 2);
        flights[flightCount++] = new Flight("SK303", "Chennai", 720, 3);

        int choice;
        // TOPICS: do-while loop and switch conditional
        do {
            System.out.println("\nFICTIONAL FLIGHT SCHEDULER");
            System.out.println("1. Display flight board");
            System.out.println("2. Search flight");
            System.out.println("3. Add flight");
            System.out.println("4. Set delay");
            System.out.println("5. Cancel flight");
            System.out.println("6. Show summary");
            System.out.println("0. Exit");
            choice = readInt("Choose: ", 0, 6);
            switch (choice) {
                case 1: displayBoard(); break;
                case 2: searchFlight(); break;
                case 3: addFlight(); break;
                case 4: setDelay(); break;
                case 5: cancelFlight(); break;
                case 6: showSummary(); break;
                case 0: System.out.println("Goodbye!"); break;
            }
        } while (choice != 0);
        input.close();
    }

    // Scanner validation avoids needing exception handling at this stage.
    static int readInt(String prompt, int minimum, int maximum) {
        while (true) {
            System.out.print(prompt);
            if (input.hasNextInt()) {
                int value = input.nextInt();
                input.nextLine();
                if (value >= minimum && value <= maximum) {
                    return value;
                }
            } else {
                input.nextLine();
            }
            System.out.println("Enter a whole number from " + minimum + " to " + maximum + ".");
        }
    }

    static String readText(String prompt) {
        String value;
        do {
            System.out.print(prompt);
            value = input.nextLine().trim();
            if (value.isEmpty()) {
                System.out.println("This value cannot be blank.");
            }
        } while (value.isEmpty());
        return value;
    }

    static String timeText(int totalMinutes) {
        int day = totalMinutes / 1440;
        int hour = (totalMinutes % 1440) / 60;
        int minute = totalMinutes % 60;
        String result = String.format("%02d:%02d", hour, minute);
        if (day > 0) {
            result = result + " (+" + day + " day)";
        }
        return result;
    }

    static void printFlight(Flight flight) {
        System.out.println(flight.number + " | " + flight.destination
                + " | Gate " + flight.gate
                + " | Scheduled " + timeText(flight.departureMinutes)
                + " | Expected " + timeText(flight.departureMinutes + flight.delayMinutes)
                + " | " + flight.status());
    }

    static void displayBoard() {
        System.out.println("\nFLIGHT BOARD");
        for (int i = 0; i < flightCount; i++) {
            printFlight(flights[i]);
        }
    }

    static int findFlight(String number) {
        for (int i = 0; i < flightCount; i++) {
            if (flights[i].number.equalsIgnoreCase(number)) {
                return i;
            }
        }
        return -1;
    }

    static void searchFlight() {
        int index = findFlight(readText("Flight number: "));
        if (index == -1) {
            System.out.println("Flight not found.");
        } else {
            printFlight(flights[index]);
        }
    }

    static void addFlight() {
        if (flightCount == flights.length) {
            System.out.println("Board is full. Maximum ten flights.");
            return;
        }
        String number = readText("New flight number: ");
        if (findFlight(number) != -1) {
            System.out.println("That flight number already exists.");
            return;
        }
        String destination = readText("Destination: ");
        int hour = readInt("Departure hour: ", 0, 23);
        int minute = readInt("Departure minute: ", 0, 59);
        int gate = readInt("Gate: ", 1, 20);
        int departure = hour * 60 + minute;
        flights[flightCount] = new Flight(number, destination, departure, gate);
        flightCount++;
        System.out.println("Flight added.");
    }

    static void setDelay() {
        int index = findFlight(readText("Flight number: "));
        if (index == -1) {
            System.out.println("Flight not found.");
        } else if (flights[index].cancelled) {
            System.out.println("A cancelled flight cannot be delayed.");
        } else {
            flights[index].delayMinutes = readInt("Total delay in minutes (0 resets): ", 0, 1440);
            System.out.println("Delay updated.");
            printFlight(flights[index]);
        }
    }

    static void cancelFlight() {
        int index = findFlight(readText("Flight number: "));
        if (index == -1) {
            System.out.println("Flight not found.");
        } else if (flights[index].cancelled) {
            System.out.println("Flight is already cancelled.");
        } else {
            flights[index].cancelled = true;
            System.out.println("Flight cancelled.");
        }
    }

    static void showSummary() {
        int cancelled = 0;
        int delayed = 0;
        for (int i = 0; i < flightCount; i++) {
            if (flights[i].cancelled) {
                cancelled++;
            } else if (flights[i].delayMinutes > 0) {
                delayed++;
            }
        }
        int onTime = flightCount - cancelled - delayed;
        double delayedPercentage = 0.0;
        if (flightCount > 0) {
            delayedPercentage = delayed * 100.0 / flightCount;
        }
        System.out.println("Total: " + flightCount + ", On time: " + onTime
                + ", Delayed: " + delayed + ", Cancelled: " + cancelled);
        System.out.printf("Delayed flights: %.1f%% of all flights%n", delayedPercentage);
    }
}
