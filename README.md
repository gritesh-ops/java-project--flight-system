Java Flight Scheduling System

A menu-driven Java console application for managing a fictional flight departure board. The project demonstrates core Java programming through flight search, schedule entry, delay updates, cancellations, and status summaries.

## Abstract

The Java Flight Scheduling System is an educational application that simulates the basic operations of an airport departure board. It provides a simple, interactive console interface through which users can view scheduled flights, search by flight number, add flight records, update delays, cancel flights, and review an overall status summary. Each flight stores its number, destination, scheduled departure time, gate, delay, and cancellation status. The application calculates expected departure times and identifies departures that move into the next day after a delay. Built using Java and its standard library, the system applies variables, data types, operators, expressions, conditional statements, loops, methods, classes, objects, and arrays to a practical problem. Input validation helps prevent invalid entries, while duplicate checks maintain unique flight numbers. Flight records are stored in memory in a fixed-size array. The project serves as a beginner-friendly demonstration of structured programming and basic object-oriented design.

## Project Description

This project models a small flight information board using fictional flight data. Users interact with a numbered menu to manage flight information during a single program session. It starts with three sample flights and supports a maximum of ten flight records.

The main purpose is to connect introductory Java concepts with a practical application that students can understand, run, and extend. It is a classroom simulation; it does not connect to live airline services.

## Objectives

- Organize flight details in a consistent structure.
- Provide simple operations for displaying, searching, and adding flights.
- Calculate expected departure times from scheduled times and delays.
- Track on-time, delayed, and cancelled flights.
- Demonstrate reusable methods, objects, arrays, and input validation.

## Key Features

| Feature | Description |
|---|---|
| Flight board | Displays flight number, destination, gate, scheduled departure, expected departure, and status. |
| Flight search | Finds a flight by number without case sensitivity. |
| Add flight | Accepts a unique flight number, destination, departure time, and gate, subject to the ten-flight limit. |
| Delay management | Sets the total delay from the original departure time; entering zero resets an active flight to on time. |
| Flight cancellation | Marks a flight as cancelled and prevents subsequent delay updates. |
| Status summary | Shows total, on-time, delayed, and cancelled counts, plus delayed flights as a percentage of all flights. |
| Next-day display | Adds a day indicator when the expected departure crosses midnight. |
| Input validation | Rejects blank text, invalid numeric input, out-of-range values, and duplicate flight numbers. |

## Technology and Design

| Component | Details |
|---|---|
| Language | Java |
| Requirement | Java Development Kit (JDK) 8 or newer |
| Interface | Interactive command-line menu |
| Input | `java.util.Scanner` |
| Storage | Fixed-size `Flight[]` array with a capacity of ten |
| Dependencies | Java standard library only; no external libraries or database |

The `Flight` class stores each flight's data and determines its status. The `FlightScheduler` class handles the menu, input validation, flight operations, time formatting, and summary calculations. Flight lookup uses a linear search through occupied array entries.

## Repository Contents

The source project is currently packaged in `Original-Flight-Scheduler .zip`. Extract the archive to access:

```text
flight-scheduler/
├── FlightScheduler.java
├── README.md
├── TOPIC_OUTPUTS.txt
├── VERIFICATION.txt
└── topics/
    ├── Topic01Variables.java
    ├── Topic02DataTypes.java
    ├── Topic03Operators.java
    ├── Topic04Expressions.java
    ├── Topic05Flowcharts.md
    ├── Topic06Conditionals.java
    ├── Topic07Loops.java
    ├── Topic08Methods.java
    ├── Topic09Objects.java
    └── Topic10Arrays.java
```

## How to Run

1. Install JDK 8 or newer and ensure `java` and `javac` are available in your terminal.
2. Download or clone this repository:

   ```bash
   git clone https://github.com/gritesh-ops/java-project--flight-system.git
   ```

3. Extract `Original-Flight-Scheduler .zip`.
4. Open a terminal inside the extracted `flight-scheduler` folder.
5. Compile and start the application:

   ```bash
   javac FlightScheduler.java
   java FlightScheduler
   ```

### Application Menu

```text
FICTIONAL FLIGHT SCHEDULER
1. Display flight board
2. Search flight
3. Add flight
4. Set delay
5. Cancel flight
6. Show summary
0. Exit
```

### Initial Flight Records

| Flight Number | Destination | Scheduled Departure | Gate |
|---|---|---|---|
| SK101 | Delhi | 09:00 | 1 |
| SK202 | Mumbai | 10:30 | 2 |
| SK303 | Chennai | 12:00 | 3 |

### Example Interaction

Setting a 35-minute delay for `SK101` produces:

```text
Choose: 4
Flight number: SK101
Total delay in minutes (0 resets): 35
Delay updated.
SK101 | Delhi | Gate 1 | Scheduled 09:00 | Expected 09:35 | Delayed
```

With the other two initial flights unchanged, the summary is:

```text
Total: 3, On time: 2, Delayed: 1, Cancelled: 0
Delayed flights: 33.3% of all flights
```

## Java Concepts Demonstrated

| Topic | Application in the Project |
|---|---|
| Variables and data types | Store flight numbers, destinations, counts, times, and cancellation flags. |
| Operators and expressions | Calculate departure times, day offsets, and percentages. |
| Flowcharts | Explain menu repetition and flight-search logic in the supporting document. |
| Conditional statements | Select menu actions and determine flight status. |
| Loops | Repeat the menu, validate input, and traverse flight records. |
| Methods | Separate display, search, add, delay, cancellation, and summary operations. |
| Classes and objects | Represent individual flights using the `Flight` class. |
| Arrays | Store and access multiple flight objects. |

The `topics` folder includes nine standalone Java examples and one flowchart document. To run an example, open a terminal in that folder and use its class name:

```bash
javac Topic01Variables.java
java Topic01Variables
```

## Rules and Current Limitations

- Flight data resets when the application restarts.
- The board holds at most ten flights, including cancelled flights.
- Departure hours must be 0–23, minutes 0–59, gates 1–20, and delays 0–1440 minutes.
- A delay replaces the previous delay; it is not added to it.
- Cancelled status takes priority over delayed status in the summary.
- Gate numbers are labels; the application does not detect gate conflicts.
- The system does not include bookings, passenger records, payments, a graphical interface, or live flight tracking.

## Possible Future Enhancements

These are proposed extensions and are not implemented in the current version:

- Save and load flight records using files or a database.
- Add a graphical interface using JavaFX or Swing.
- Support editing and removing flight records.
- Add departure dates, sorting, and destination filters.
- Replace the fixed array with a dynamic collection.
- Detect gate conflicts and overlapping schedules.

## Acknowledgment

The README included in the source archive credits the flight-board theme of [pspj-flight-scheduler](https://github.com/nckumaruoh/pspj-flight-scheduler) and states that the implementation was newly written without copying that repository's source code. All flight details in this project are fictional.
