# Airport Flight Board and Scheduler Code by Topic

The code is separated by individual topics. Each topic includes the actual application code, its source location, why it is used and a short viva answer. A section can also contain supporting concepts because useful Java operations combine them.

Each numbered file under topic-code is a study excerpt with the extension .java.txt. These excerpts refer to the stated class and are not independent programs. The complete runnable code is in src. Compile that folder using build.bat or build.sh. Do not paste the excerpts together because some sections intentionally repeat code to explain a different concept.

## Topic Index

| Number | Topic | Separate code file |
|---|---|---|
| 1 | Variables Constants and Primitive Data Types | 01_Variables_Constants_and_Data_Types.java.txt |
| 2 | Classes Objects and Fields | 02_Classes_Objects_and_Fields.java.txt |
| 3 | Parameterized Constructor and this | 03_Constructor_and_this.java.txt |
| 4 | Encapsulation Accessors and Controlled Mutation | 04_Encapsulation_Getters_and_Mutator.java.txt |
| 5 | Scanner and Console Input | 05_Scanner_and_Console_Input.java.txt |
| 6 | while Loops and Numeric Input Validation | 06_While_Loop_and_Input_Validation.java.txt |
| 7 | Strings Arithmetic Operators and Time Conversion | 07_Strings_Arithmetic_and_Time.java.txt |
| 8 | String Length Indexing Trimming and Validation | 08_String_Operations_and_Validation.java.txt |
| 9 | One Dimensional Arrays and Adding a Flight | 09_One_Dimensional_Array_and_Insertion.java.txt |
| 10 | for Loop Linear Search equals and null | 10_For_Loop_and_Linear_Search.java.txt |
| 11 | Methods Parameters Return Values and throw | 11_Methods_Parameters_and_Returns.java.txt |
| 12 | Relational Logical Operators and continue | 12_Gate_Clash_Conditions_and_Continue.java.txt |
| 13 | if else and Delay Calculation | 13_If_Else_and_Delay.java.txt |
| 14 | Gate Changes through Reusable Methods | 14_Gate_Update_Method.java.txt |
| 15 | Status Comparison and Conditional Validation | 15_Status_Comparison_and_Update.java.txt |
| 16 | Interfaces Comparator and Method Overriding | 16_Comparator_Interface_and_Overriding.java.txt |
| 17 | Library Sorting with Arrays sort | 17_Arrays_Sort.java.txt |
| 18 | Enhanced for Each Loop and Formatted Printing | 18_For_Each_and_Formatted_Board.java.txt |
| 19 | Counting Summation Nested if Average and Typecasting | 19_Count_Sum_Average_and_Typecast.java.txt |
| 20 | toString String Concatenation and Search Output | 20_ToString_and_Search_Output.java.txt |
| 21 | File Reading Path Files split and throws | 21_File_Reading_and_Record_Parsing.java.txt |
| 22 | File Writing join Resources and finally | 22_File_Writing_and_Resource_Management.java.txt |
| 23 | try catch and Checked Exception Handling | 23_Try_Catch_and_Save_Errors.java.txt |
| 24 | Program Structure main switch break and Menu Loop | 24_Main_Switch_and_Menu.java.txt |

## 1 Variables Constants and Primitive Data Types

**Source location:** FlightScheduler fields and AirportApp summary.

**Why used:** Store flight capacity, gate separation, array size, counts and numeric results with explicit types.

```java
    public static final int MAX_FLIGHTS = 200;
    public static final int GATE_GAP_MINUTES = 30;
    private final Flight[] flights = new Flight[MAX_FLIGHTS];
    private int count = 0;

double averageDelay = 0.0;
```

**Viva answer:** int stores minutes/counts; boolean stores decisions; double keeps fractional averages. static final names shared constants.

## 2 Classes Objects and Fields

**Source location:** Flight fields and AirportApp.addFlight.

**Why used:** Represent each flight as an object with named properties instead of unrelated variables.

```java
    private final String number;
    private final String airline;
    private final String origin;
    private final String destination;
    private final int scheduledMinutes;
    private int expectedMinutes;
    private String gate;
    private String status;

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
```

**Viva answer:** Flight is the blueprint; new Flight creates one instance. Separate objects retain separate flight details.

## 3 Parameterized Constructor and this

**Source location:** Flight constructor.

**Why used:** Require valid identifying details, route, times, gate and status when a flight is created.

```java
    public Flight(String number, String airline, String origin, String destination,
                  int scheduledMinutes, int expectedMinutes, String gate, String status) {
        this.number = cleanText(number, "Flight number").toUpperCase(Locale.ROOT);
        if (!this.number.matches("[A-Z0-9]{2,10}")) {
            throw new IllegalArgumentException("Flight number must contain 2 to 10 letters/digits.");
        }
        this.airline = cleanText(airline, "Airline");
        this.origin = cleanText(origin, "Origin");
        this.destination = cleanText(destination, "Destination");
        if (this.origin.equalsIgnoreCase(this.destination)) {
            throw new IllegalArgumentException("Origin and destination must be different.");
        }
        validateMinutes(scheduledMinutes);
        this.scheduledMinutes = scheduledMinutes;
        updateBoardDetails(expectedMinutes, gate, status);
    }
```

**Viva answer:** The constructor has no return type. this.number is the field of the current object; number is its parameter.

## 4 Encapsulation Accessors and Controlled Mutation

**Source location:** Flight accessors and updateBoardDetails.

**Why used:** Keep fields private and update expected time, gate and status together after validation.

```java
    public String getNumber() { return number; }
    public String getAirline() { return airline; }
    public String getOrigin() { return origin; }
    public String getDestination() { return destination; }
    public int getScheduledMinutes() { return scheduledMinutes; }
    public int getExpectedMinutes() { return expectedMinutes; }
    public String getGate() { return gate; }
    public String getStatus() { return status; }
    public int getDelayMinutes() { return expectedMinutes - scheduledMinutes; }
    public boolean isActive() { return isActiveStatus(status); }

    void updateBoardDetails(int expectedMinutes, String gate, String status) {
        validateMinutes(expectedMinutes);
        String checkedGate = normalizeGate(gate);
        String checkedStatus = cleanText(status, "Status").toUpperCase(Locale.ROOT);
        if (!isKnownStatus(checkedStatus)) {
            throw new IllegalArgumentException("Unknown status: " + checkedStatus);
        }
        if (expectedMinutes < scheduledMinutes) {
            throw new IllegalArgumentException("Expected time cannot be earlier than scheduled time.");
        }
        if (SCHEDULED.equals(checkedStatus) && expectedMinutes != scheduledMinutes) {
            throw new IllegalArgumentException("A delayed flight cannot be marked SCHEDULED. Reset its delay first.");
        }
        if (DELAYED.equals(checkedStatus) && expectedMinutes == scheduledMinutes) {
            throw new IllegalArgumentException("DELAYED requires a positive delay. Use the delay option first.");
        }
        this.expectedMinutes = expectedMinutes;
        this.gate = checkedGate;
        this.status = checkedStatus;
    }
```

**Viva answer:** Getters expose values. Normal edits go through FlightScheduler, which checks gate conflicts before calling the package-level mutator.

## 5 Scanner and Console Input

**Source location:** ConsoleInput field constructor and readText.

**Why used:** Read operator-entered flight details and menu values from the console.

```java
import java.util.Scanner;
import java.util.NoSuchElementException;

    private final Scanner scanner;

    public ConsoleInput(Scanner scanner) {
        this.scanner = scanner;
    }

    public String readText(String prompt) {
        System.out.print(prompt);
        if (!scanner.hasNextLine()) {
            throw new NoSuchElementException("End of console input.");
        }
        return scanner.nextLine().trim();
    }
```

**Viva answer:** Read a complete line, then parse it. This avoids the leftover-newline issue from mixing nextInt with nextLine.

## 6 while Loops and Numeric Input Validation

**Source location:** ConsoleInput.readInt and readTime.

**Why used:** Repeat input until it is a valid integer or time, rather than terminating on a typing mistake.

```java
    public int readInt(String prompt, int minimum, int maximum) {
        while (true) {
            String text = readText(prompt);
            try {
                int value = Integer.parseInt(text);
                if (value >= minimum && value <= maximum) {
                    return value;
                }
                System.out.println("Enter a number from " + minimum + " to " + maximum + ".");
            } catch (NumberFormatException exception) {
                System.out.println("Enter a whole number.");
            }
        }
    }

    public int readTime(String prompt) {
        while (true) {
            String text = readText(prompt);
            try {
                return Flight.parseTime(text);
            } catch (IllegalArgumentException exception) {
                System.out.println(exception.getMessage());
            }
        }
    }
```

**Viva answer:** while(true) has controlled exits: return for valid input and an end-of-input exception. It is not an endless application loop.

## 7 Strings Arithmetic Operators and Time Conversion

**Source location:** Flight.parseTime and formatTime.

**Why used:** Turn HH:mm into minutes for comparisons and turn minutes back into display text.

```java
    public static int parseTime(String text) {
        if (text == null || !text.matches("[0-9]{2}:[0-9]{2}")) {
            throw new IllegalArgumentException("Enter time in HH:mm format, for example 09:30.");
        }
        int hour = Integer.parseInt(text.substring(0, 2));
        int minute = Integer.parseInt(text.substring(3, 5));
        if (hour > 23 || minute > 59) {
            throw new IllegalArgumentException("Hour must be 00 to 23 and minute 00 to 59.");
        }
        return hour * 60 + minute;
    }

    public static String formatTime(int minutes) {
        validateMinutes(minutes);
        return String.format(Locale.ROOT, "%02d:%02d", minutes / 60, minutes % 60);
    }
```

**Viva answer:** 09:30 becomes 570. Division gets hours and remainder gets minutes. substring extracts the two text components.

## 8 String Length Indexing Trimming and Validation

**Source location:** Flight.cleanText and normalizeGate.

**Why used:** Reject empty/unsafe fields and normalize gates before storing or comparing them.

```java
    private static String cleanText(String value, String field) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(field + " cannot be empty.");
        }
        String text = value.trim();
        if (text.length() > 40 || text.contains("|") || text.contains("\n") || text.contains("\r")) {
            throw new IllegalArgumentException(field + " must be at most 40 characters and contain no | or line breaks.");
        }
        for (int i = 0; i < text.length(); i++) {
            if (Character.isISOControl(text.charAt(i))) {
                throw new IllegalArgumentException(field + " cannot contain control characters.");
            }
        }
        return text;
    }

    public static String normalizeGate(String gate) {
        String normalized = cleanText(gate, "Gate").toUpperCase(Locale.ROOT);
        if (!normalized.matches("[A-Z][0-9]{1,2}")) {
            throw new IllegalArgumentException("Use a gate such as A1, B2 or C12.");
        }
        return normalized;
    }
```

**Viva answer:** trim removes surrounding spaces; length checks limits; contains finds forbidden delimiters; charAt examines characters; uppercase normalizes keys.

## 9 One Dimensional Arrays and Adding a Flight

**Source location:** FlightScheduler fields and addFlight.

**Why used:** Keep a bounded board of 200 flight references and insert a validated record at the next free position.

```java
    public static final int MAX_FLIGHTS = 200;
    public static final int GATE_GAP_MINUTES = 30;
    private final Flight[] flights = new Flight[MAX_FLIGHTS];
    private int count = 0;

    public void addFlight(Flight flight) {
        if (flight == null) {
            throw new IllegalArgumentException("Flight cannot be null.");
        }
        if (count == MAX_FLIGHTS) {
            throw new IllegalArgumentException("Board is full. Maximum: " + MAX_FLIGHTS);
        }
        if (findFlight(flight.getNumber()) != null) {
            throw new IllegalArgumentException("Duplicate flight number: " + flight.getNumber());
        }
        checkGate(flight.getGate(), flight.getExpectedMinutes(), flight.getStatus(), null);
        flights[count] = flight;
        count++;
    }
```

**Viva answer:** Valid occupied positions are 0 through count minus 1. count++ advances the next insertion position.

## 10 for Loop Linear Search equals and null

**Source location:** FlightScheduler.findFlight.

**Why used:** Find an existing flight by number and detect duplicate numbers with one simple scan.

```java
    public Flight findFlight(String number) {
        if (number == null) {
            return null;
        }
        String query = number.trim().toUpperCase(Locale.ROOT);
        for (int i = 0; i < count; i++) {
            if (flights[i].getNumber().equals(query)) {
                return flights[i];
            }
        }
        return null;
    }
```

**Viva answer:** equals compares String contents; null means no record was found. Linear search takes O(n) time.

## 11 Methods Parameters Return Values and throw

**Source location:** FlightScheduler.requireFlight and Flight.validateMinutes.

**Why used:** Reuse lookup and validation rules, returning a result or reporting a precise failure.

```java
    private Flight requireFlight(String number) {
        Flight found = findFlight(number);
        if (found == null) {
            throw new IllegalArgumentException("Flight not found: " + number);
        }
        return found;
    }

    private static void validateMinutes(int minutes) {
        if (minutes < 0 || minutes >= 24 * 60) {
            throw new IllegalArgumentException("Time must be within this day, 00:00 to 23:59.");
        }
    }
```

**Viva answer:** Java passes values; an object argument passes a copy of its reference value. throw raises an exception at the point of failure.

## 12 Relational Logical Operators and continue

**Source location:** FlightScheduler.checkGate.

**Why used:** Reject active flights at the same gate when their expected departure times are less than 30 minutes apart.

```java
    private void checkGate(String gate, int expected, String status, String ignoredNumber) {
        if (!Flight.isActiveStatus(status)) {
            return;
        }
        for (int i = 0; i < count; i++) {
            Flight other = flights[i];
            if (!other.isActive() || other.getNumber().equals(ignoredNumber)) {
                continue;
            }
            int difference = expected - other.getExpectedMinutes();
            if (other.getGate().equals(gate)
                    && difference > -GATE_GAP_MINUTES && difference < GATE_GAP_MINUTES) {
                throw new IllegalArgumentException("Gate clash with " + other.getNumber()
                        + " at " + Flight.formatTime(other.getExpectedMinutes())
                        + ". Keep at least " + GATE_GAP_MINUTES + " minutes between active flights.");
            }
        }
    }
```

**Viva answer:** continue skips inactive/self records. AND combines same-gate and time conditions. Exactly 30 minutes is accepted.

## 13 if else and Delay Calculation

**Source location:** FlightScheduler.setDelay.

**Why used:** Choose SCHEDULED for zero delay and DELAYED for a positive delay, checking the proposed update first.

```java
    public void setDelay(String number, int totalDelayMinutes) {
        Flight flight = requireFlight(number);
        if (!flight.isActive()) {
            throw new IllegalArgumentException("Cannot delay a departed or cancelled flight.");
        }
        if (totalDelayMinutes < 0 || totalDelayMinutes > 24 * 60 - 1 - flight.getScheduledMinutes()) {
            throw new IllegalArgumentException("Delay must keep the expected time within this day.");
        }
        int expected = flight.getScheduledMinutes() + totalDelayMinutes;
        String status;
        if (totalDelayMinutes == 0) {
            status = Flight.SCHEDULED;
        } else {
            status = Flight.DELAYED;
        }
        checkGate(flight.getGate(), expected, status, flight.getNumber());
        flight.updateBoardDetails(expected, flight.getGate(), status);
    }
```

**Viva answer:** Delay means total minutes after the original scheduled time. Rejected edits do not alter the existing flight.

## 14 Gate Changes through Reusable Methods

**Source location:** FlightScheduler.changeGate.

**Why used:** Reuse gate normalization and clash detection when the operator assigns another gate.

```java
    public void changeGate(String number, String gate) {
        Flight flight = requireFlight(number);
        if (!flight.isActive()) {
            throw new IllegalArgumentException("Cannot change the gate of a departed or cancelled flight.");
        }
        String checkedGate = Flight.normalizeGate(gate);
        checkGate(checkedGate, flight.getExpectedMinutes(), flight.getStatus(), flight.getNumber());
        flight.updateBoardDetails(flight.getExpectedMinutes(), checkedGate, flight.getStatus());
    }
```

**Viva answer:** Changing a field directly would bypass a cross-record rule. The scheduler validates the proposed gate before mutation.

## 15 Status Comparison and Conditional Validation

**Source location:** Flight status helpers and FlightScheduler.changeStatus.

**Why used:** Accept only known status values and recheck gate separation when a record becomes active.

```java
    public static boolean isKnownStatus(String status) {
        return SCHEDULED.equals(status) || DELAYED.equals(status) || BOARDING.equals(status)
                || DEPARTED.equals(status) || CANCELLED.equals(status);
    }

    public static boolean isActiveStatus(String status) {
        return SCHEDULED.equals(status) || DELAYED.equals(status) || BOARDING.equals(status);
    }

    public void changeStatus(String number, String status) {
        Flight flight = requireFlight(number);
        if (status == null) {
            throw new IllegalArgumentException("Status cannot be null.");
        }
        String checkedStatus = status.trim().toUpperCase(Locale.ROOT);
        if (!Flight.isKnownStatus(checkedStatus)) {
            throw new IllegalArgumentException("Choose SCHEDULED, DELAYED, BOARDING, DEPARTED or CANCELLED.");
        }
        // Reinstating a cancelled/departed record is an operator correction.
        // It must pass the same gate check as a new active flight.
        checkGate(flight.getGate(), flight.getExpectedMinutes(), checkedStatus, flight.getNumber());
        flight.updateBoardDetails(flight.getExpectedMinutes(), flight.getGate(), checkedStatus);
    }
```

**Viva answer:** SCHEDULED, DELAYED and BOARDING are active; DEPARTED and CANCELLED release a gate. Use equals for String values.

## 16 Interfaces Comparator and Method Overriding

**Source location:** FlightTimeComparator.

**Why used:** Supply a flight ordering policy to the library without adding a custom Flight inheritance hierarchy.

```java
import java.util.Comparator;

/** Topics: interfaces, overriding, Comparator and sorting. */
public class FlightTimeComparator implements Comparator<Flight> {
    @Override
    public int compare(Flight first, Flight second) {
        if (first.getExpectedMinutes() < second.getExpectedMinutes()) {
            return -1;
        }
        if (first.getExpectedMinutes() > second.getExpectedMinutes()) {
            return 1;
        }
        // Flight number is the tie breaker when times are equal.
        return first.getNumber().compareTo(second.getNumber());
    }
}
```

**Viva answer:** The comparator is-a Comparator of Flight. compare returns negative, positive or zero according to ordering; flight number breaks equal-time ties.

## 17 Library Sorting with Arrays sort

**Source location:** FlightScheduler.getSortedFlights.

**Why used:** Display flights in expected-time order while preserving the backing array element order.

```java
    public Flight[] getSortedFlights() {
        Flight[] board = Arrays.copyOf(flights, count);
        Arrays.sort(board, new FlightTimeComparator());
        return board;
    }
```

**Viva answer:** Arrays.copyOf copies occupied array slots; it is not a deep copy of Flight objects. Arrays.sort uses the Comparator.

## 18 Enhanced for Each Loop and Formatted Printing

**Source location:** AirportApp.printBoard.

**Why used:** Display every flight with aligned columns without managing a manual array index.

```java
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
```

**Viva answer:** printf formats columns. The board shortens long names; search prints the full values.

## 19 Counting Summation Nested if Average and Typecasting

**Source location:** AirportApp.printSummary.

**Why used:** Show board totals and an average active-flight delay without losing fractional results.

```java
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
```

**Viva answer:** The denominator is all active flights, including zero-delay flights. (double) prevents integer division; active > 0 prevents division by zero.

## 20 toString String Concatenation and Search Output

**Source location:** Flight.toString and AirportApp.findFlight.

**Why used:** Provide a readable complete description when a flight is found.

```java
    @Override
    public String toString() {
        return number + " | " + airline + " | " + origin + " -> " + destination
                + " | Scheduled " + formatTime(scheduledMinutes)
                + " | Expected " + formatTime(expectedMinutes)
                + " | Gate " + gate + " | " + status;
    }

    private static void findFlight(ConsoleInput input, FlightScheduler scheduler) {
        Flight found = scheduler.findFlight(input.readText("Flight number: "));
        if (found == null) {
            System.out.println("No matching flight.");
        } else {
            System.out.println(found); // Calls the overridden toString method.
        }
    }
```

**Viva answer:** toString overrides Object representation. Printing the Flight reference calls its toString method.

## 21 File Reading Path Files split and throws

**Source location:** FlightFileStore.load.

**Why used:** Restore saved records line by line and detect malformed fields, duplicates and gate clashes.

```java
    private static final String HEADER = "number|airline|origin|destination|scheduled|expected|gate|status";

    public static FlightScheduler load(Path file) throws IOException {
        FlightScheduler loaded = new FlightScheduler();
        // A missing file means a new, empty board. Other I/O errors are fatal.
        if (Files.notExists(file)) {
            return loaded;
        }
        try (BufferedReader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            if (!HEADER.equals(reader.readLine())) {
                throw new IOException("Invalid file header. Expected: " + HEADER);
            }
            String line;
            int lineNumber = 1;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (line.trim().isEmpty()) {
                    continue;
                }
                try {
                    String[] fields = line.split("\\|", -1);
                    if (fields.length != 8) {
                        throw new IllegalArgumentException("Expected 8 pipe-separated fields.");
                    }
                    Flight flight = new Flight(fields[0], fields[1], fields[2], fields[3],
                            Flight.parseTime(fields[4]), Flight.parseTime(fields[5]), fields[6], fields[7]);
                    loaded.addFlight(flight);
                } catch (IllegalArgumentException exception) {
                    // Abort safely so malformed source data is never overwritten.
                    throw new IOException("Invalid record at line " + lineNumber + ": "
                            + exception.getMessage(), exception);
                }
            }
        }
        return loaded;
    }
```

**Viva answer:** IOException is checked. split tokenizes fields; the escaped pipe is a delimiter pattern, not a bitwise operator. A bad record stops startup without changing the file.

## 22 File Writing join Resources and finally

**Source location:** FlightFileStore.save.

**Why used:** Write a complete snapshot, close the writer, then replace the destination; clean up a leftover temporary file.

```java
    public static void save(Path file, FlightScheduler scheduler) throws IOException {
        Path target = file.toAbsolutePath();
        Files.createDirectories(target.getParent());
        Path temporary = Files.createTempFile(target.getParent(), "flight-board-", ".tmp");
        try {
            try (BufferedWriter writer = Files.newBufferedWriter(temporary, StandardCharsets.UTF_8)) {
                writer.write(HEADER);
                writer.newLine();
                for (Flight flight : scheduler.getSortedFlights()) {
                    String record = String.join("|", flight.getNumber(), flight.getAirline(),
                            flight.getOrigin(), flight.getDestination(),
                            Flight.formatTime(flight.getScheduledMinutes()),
                            Flight.formatTime(flight.getExpectedMinutes()), flight.getGate(), flight.getStatus());
                    writer.write(record);
                    writer.newLine();
                }
            }
            try {
                Files.move(temporary, target, StandardCopyOption.REPLACE_EXISTING,
                        StandardCopyOption.ATOMIC_MOVE);
            } catch (AtomicMoveNotSupportedException exception) {
                // Some file systems do not support atomic replacement.
                Files.move(temporary, target, StandardCopyOption.REPLACE_EXISTING);
            }
        } finally {
            Files.deleteIfExists(temporary);
        }
    }
```

**Viva answer:** String.join builds one record. try-with-resources closes the writer; finally attempts cleanup. Atomic replacement is requested with a fallback when unsupported.

## 23 try catch and Checked Exception Handling

**Source location:** AirportApp.save.

**Why used:** Report storage failure and let the operator retry without discarding in-memory changes.

```java
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
```

**Viva answer:** IOException must be handled or declared. The helper returns true or false so normal exit can depend on successful saving.

## 24 Program Structure main switch break and Menu Loop

**Source location:** AirportApp.main and printMenu.

**Why used:** Connect all operations, repeat the menu and stop after a successful save-and-exit operation.

```java
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
```

**Viva answer:** main is the entry point. switch selects one menu operation; break prevents case fall-through. The running flag controls repetition.

## Topics Not Used and Why

| Topic | Why left out |
|---|---|
| Recursion and its base and recursive cases | A flat board needs iterative scans, not recursive dependencies or tree traversal |
| Two dimensional arrays and matrix arithmetic | Flight objects contain named mixed-type fields; the task is not a numeric matrix problem |
| Bitwise operators | No binary flags, bit packing or hardware registers are required |
| Ternary operator | The explicit if-else used for delay status is clear enough |
| do-while | The existing while forms handle menu/input repetition; another loop form adds no requirement |
| Nested loops | Each edit needs one gate scan; no directly nested pairwise scan is written |
| Endless loops without an exit | The operator must be able to save and exit; validation loops return or throw on end of input |
| Student-defined method overloading | One signature per operation meets this version's needs |
| Explicit empty Flight constructor | A Flight must have valid identity, route, time, gate and status at creation |
| Constructor overloading | One full constructor works for both file-loaded and operator-entered records |
| Custom single, multilevel, hierarchical or hybrid inheritance | No distinct flight subtype behavior is required. Object inheritance and standard interface implementation still exist |
| Multiple class inheritance | Java does not support extending multiple classes; multiple interface roles are unnecessary here |
| A new project-specific interface | The standard Comparator interface already provides the ordering contract |
| instanceof | Only Flight records are stored, so subtype tests are unnecessary |
| Multi-catch | The handlers need one relevant exception family or a specific I/O exception |
| Catching Error | Ordinary invalid input is an exception; this project does not try to recover from severe runtime failures |
| String value comparison using double equals | equals compares content. Double equals is used appropriately for primitive values and null checks |
| StringBuilder | Messages are short fixed constructions; there is no large repeated text-building loop |
| StringBuffer | There is no shared mutable text used by multiple threads |
| Collections.sort | Records are stored in an array rather than a List |
| Arrays.binarySearch | The board is sorted by expected time but searched by flight number; a compatible search ordering is absent and n is at most 200 |
| Flight implements Comparable | Comparator keeps the selected board order separate from the Flight class |
| Whole-file loading | Buffered line-by-line processing identifies each malformed record's line |
| Appending update records | A snapshot should have one current row per ID; append would need an event-log resolution design |
| byte short long and float variables | int fits single-day minutes and small counts; double suits the average |
| Lowercase conversion as an additional key operation | Uppercase normalization already gives consistent keys |

Java environment setup, algorithms, flowcharts, identifiers, literals, precedence and associativity support the implementation. They do not need artificial extra source files. The runnable classes show the Java structure, and PROJECT_EXPLANATION.md gives the algorithms and flowcharts. Ordinary operator precedence applies to formulas such as hour * 60 + minute; parentheses and simple expressions keep the intended evaluation clear.

## Important Viva Distinctions

- Java is pass-by-value. An object argument passes a copy of a reference value.
- FlightTimeComparator implements a standard interface. There is no custom Flight inheritance hierarchy.
- Arrays.copyOf copies array positions; Flight objects remain shared references.
- A total delay of 10 minutes means scheduled time plus 10, even if an earlier update set 15.
- The average includes all active flights, including those with zero delay.
- The 30-minute separation rule is a classroom assumption, not a claim about an airport regulation.
- This is a single-day departure-board simulation. It does not obtain live data or optimize runway allocation.

## Read and Run the Complete Code

The src folder has six complete classes: AirportApp, ConsoleInput, Flight, FlightScheduler, FlightTimeComparator and FlightFileStore. Imports and class boundaries are preserved there. The excerpt files intentionally omit unrelated class sections when the topic is a single method.

Extract the project, open its folder and run run.bat, or use:

```text
java -jar Airport_Flight_Board_Scheduler.jar
```

To build from source with JDK 9 or later, run build.bat on Windows or sh build.sh on Linux/macOS. The build targets Java 8. See README.md for the JDK 8 compile alternative.
