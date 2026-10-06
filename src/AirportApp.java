import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.NoSuchElementException;
import java.util.Scanner;

/** Entry point: Module 1 input/output and Module 2 menu control. */
public class AirportApp {
    public static void main(String[] args) {
        Path file;
        if (args.length == 0) {
            file = Paths.get("data", "flights.txt");
        } else if (args.length == 1) {
            file = Paths.get(args[0]);
        } else {
            System.out.println("Usage: java -jar Airport_Flight_Board_Scheduler.jar [data-file]");
            return;
        }

        FlightScheduler scheduler;
        try {
            boolean missing = Files.notExists(file);
            scheduler = FlightFileStore.load(file);
            System.out.println("AIRPORT FLIGHT BOARD & SCHEDULER");
            System.out.println("Single-day teaching demo | Local 24-hour time | Gate gap: 30 minutes");
            if (missing) {
                System.out.println("No data file found. Starting an empty board.");
            }
            System.out.println("Loaded flights: " + scheduler.size());
            System.out.println("Data file: " + file.toAbsolutePath());
        } catch (IOException exception) {
            System.out.println("Cannot load data: " + exception.getMessage());
            System.out.println("Correct the file and restart. The original file was not changed.");
            return;
        }

        try (Scanner scanner = new Scanner(System.in)) {
            ConsoleInput input = new ConsoleInput(scanner);
            boolean running = true;
            while (running) {
                printMenu();
                try {
                    int choice = input.readInt("Choose: ", 0, 8);
                    switch (choice) {
                        case 1:
                            printBoard(scheduler);
                            break;
                        case 2:
                            addFlight(input, scheduler);
                            break;
                        case 3:
                            findFlight(input, scheduler);
                            break;
                        case 4:
                            String delayNumber = input.readText("Flight number: ");
                            int delay = input.readInt("TOTAL delay after scheduled time (minutes): ", 0, 1439);
                            scheduler.setDelay(delayNumber, delay);
                            System.out.println("Delay updated. Set BOARDING again if needed.");
                            break;
                        case 5:
                            String gateNumber = input.readText("Flight number: ");
                            String gate = input.readText("New gate: ");
                            scheduler.changeGate(gateNumber, gate);
                            System.out.println("Gate updated.");
                            break;
                        case 6:
                            String statusNumber = input.readText("Flight number: ");
                            System.out.println("SCHEDULED / DELAYED / BOARDING / DEPARTED / CANCELLED");
                            scheduler.changeStatus(statusNumber, input.readText("New status: "));
                            System.out.println("Status updated.");
                            break;
                        case 7:
                            printSummary(scheduler);
                            break;
                        case 8:
                            save(file, scheduler);
                            break;
                        case 0:
                            if (save(file, scheduler)) {
                                running = false;
                                System.out.println("Goodbye.");
                            }
                            break;
                        default:
                            System.out.println("Unknown menu option.");
                    }
                } catch (NoSuchElementException exception) {
                    System.out.println("\nEnd of input. Saving current board.");
                    save(file, scheduler);
                    running = false;
                } catch (IllegalArgumentException exception) {
                    System.out.println("Action rejected: " + exception.getMessage());
                }
            }
        }
    }

    private static void printMenu() {
        System.out.println("\n1 Show flight board    2 Add flight       3 Search by flight number");
        System.out.println("4 Set/reset delay      5 Change gate      6 Change status");
        System.out.println("7 Show summary         8 Save             0 Save and exit");
    }

    private static void addFlight(ConsoleInput input, FlightScheduler scheduler) {
        String number = input.readText("Flight number: ");
        String airline = input.readText("Airline: ");
        String origin = input.readText("Origin: ");
        String destination = input.readText("Destination: ");
        int scheduled = input.readTime("Scheduled time (HH:mm): ");
        String gate = input.readText("Gate: ");
        Flight flight = new Flight(number, airline, origin, destination,
                scheduled, scheduled, gate, Flight.SCHEDULED);
        scheduler.addFlight(flight);
        System.out.println("Flight added.");
    }

    private static void findFlight(ConsoleInput input, FlightScheduler scheduler) {
        Flight found = scheduler.findFlight(input.readText("Flight number: "));
        if (found == null) {
            System.out.println("No matching flight.");
        } else {
            System.out.println(found); // Calls the overridden toString method.
        }
    }

    private static void printBoard(FlightScheduler scheduler) {
        System.out.println("\nDEPARTURE BOARD - ordered by expected time, then flight number");
        System.out.printf("%-10s %-15s %-16s %-16s %-5s %-5s %-4s %-9s%n",
                "FLIGHT", "AIRLINE", "ORIGIN", "DESTINATION", "SCHED", "EXP", "GATE", "STATUS");
        for (Flight flight : scheduler.getSortedFlights()) {
            System.out.printf("%-10s %-15.15s %-16.16s %-16.16s %-5s %-5s %-4s %-9s%n",
                    flight.getNumber(), flight.getAirline(), flight.getOrigin(), flight.getDestination(),
                    Flight.formatTime(flight.getScheduledMinutes()),
                    Flight.formatTime(flight.getExpectedMinutes()), flight.getGate(), flight.getStatus());
        }
        if (scheduler.size() == 0) {
            System.out.println("No flights scheduled.");
        }
        System.out.println("Long airline/city names are shortened on this board; search shows full details.");
    }

    private static void printSummary(FlightScheduler scheduler) {
        int active = 0;
        int departed = 0;
        int cancelled = 0;
        int delayedActive = 0;
        int totalDelay = 0;
        for (Flight flight : scheduler.getSortedFlights()) {
            if (flight.isActive()) {
                active++;
                totalDelay += flight.getDelayMinutes();
                if (flight.getDelayMinutes() > 0) {
                    delayedActive++;
                }
            } else if (Flight.DEPARTED.equals(flight.getStatus())) {
                departed++;
            } else {
                cancelled++;
            }
        }
        double averageDelay = 0.0;
        if (active > 0) {
            averageDelay = (double) totalDelay / active;
        }
        System.out.printf("Total: %d | Active: %d | Departed: %d | Cancelled: %d%n",
                scheduler.size(), active, departed, cancelled);
        System.out.printf("Active flights with delay: %d | Total active delay: %d minutes%n",
                delayedActive, totalDelay);
        System.out.printf("Average delay across ALL active flights: %.2f minutes%n", averageDelay);
    }

    private static boolean save(Path file, FlightScheduler scheduler) {
        try {
            FlightFileStore.save(file, scheduler);
            System.out.println("Saved " + scheduler.size() + " flights.");
            return true;
        } catch (IOException exception) {
            System.out.println("Save failed: " + exception.getMessage());
            System.out.println("Changes are still in memory. Check file permissions and retry option 8.");
            return false;
        }
    }
}
