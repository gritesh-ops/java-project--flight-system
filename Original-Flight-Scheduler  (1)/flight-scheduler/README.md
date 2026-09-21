# Original Java Flight Scheduler: Topics 1–10

This is a newly written educational program based on the flight-board theme of https://github.com/nckumaruoh/pspj-flight-scheduler. No repository source code was copied. Its current README describes a classroom board simulator; this version uses fictional flights to match the requested project theme.

## What is included

- `FlightScheduler.java`: complete interactive program combining the topics.
- `topics/`: nine small runnable Java examples and a flowchart document, separated in your requested learning order.
- Only introductory Java concepts are used: variables, types, operators, expressions, decisions, loops, methods, classes/objects, and fixed-size arrays. Scanner supplies keyboard input.

## Run the complete program

Install a Java JDK (Java 8 or newer). Open a terminal in this folder:

```text
javac FlightScheduler.java
java FlightScheduler
```

Choose a number from the menu. Initial flight numbers are SK101, SK202, and SK303.

## Run a separate topic

Open a terminal in the `topics` folder, then use its matching class name:

```text
javac Topic01Variables.java
java Topic01Variables
```

Repeat with any Java example listed below. Flowcharts are diagrams, so topic 05 has no Java class.

## Topic-by-topic explanation

| Topic | Separate file | Meaning and use in the full program |
|---|---|---|
| 01. Variables | `Topic01Variables.java` | Named values that can change. `flightCount` tracks how many flights are stored. |
| 02. Data types | `Topic02DataTypes.java` | `int` stores minutes and gates; `double` stores percentages; `boolean` stores cancellation; `String` stores text. The separate example also demonstrates `char`. |
| 03. Operators | `Topic03Operators.java` | `+`, `-`, `*`, `/`, `%` calculate values; `==`, `!=`, `>`, `<`, `>=`, `<=` compare; `&&` combines conditions; `++` increments. `=` assigns a value. |
| 04. Expressions | `Topic04Expressions.java` | An expression produces a value. `hour * 60 + minute` converts a time into total minutes; `delayed * 100.0 / flightCount` produces a percentage. |
| 05. Flowcharts | `Topic05Flowcharts.md` | Diagrams show menu repetition and the array-search algorithm. |
| 06. Conditional statements | `Topic06Conditionals.java` | `if/else` chooses status; `switch` selects a menu action. |
| 07. Loops | `Topic07Loops.java` | `for` visits stored flights; `while` retries numeric input; `do-while` repeats the menu at least once. |
| 08. Methods | `Topic08Methods.java` | Named reusable operations. `findFlight` returns an array index; `displayBoard` prints without returning a value (`void`). Arguments provide input to methods. |
| 09. Objects | `Topic09Objects.java` | A class defines fields and behavior. `new Flight(...)` constructs an object. Each flight has its own number, gate, delay, and status. `this` refers to the current object. |
| 10. Arrays | `Topic10Arrays.java` | An array stores multiple values of one type, using zero-based indexes. `Flight[]` holds up to ten references to Flight objects. |

### Important details

- `flights.length` is the capacity (10); `flightCount` is the occupied count (initially 3). Only occupied entries are read because unused object-array entries contain `null`.
- `findFlight` performs a linear search and returns `-1` when no match exists. Text is compared using `equalsIgnoreCase`, not `==`.
- Constructors initialize new objects. `static` methods belong to the scheduler class; `status()` belongs to each flight object.
- A delay is the total delay from the original departure, not an additional increment. Setting it to zero restores “On time” for an active flight.
- Cancelled flights take priority over delayed status and count only as cancelled in the summary.
- Times are entered as separate hour and minute values. A delay past midnight displays `(+1 day)`.

## Features and scope

Display all flights; search without case sensitivity; add flights with duplicate and capacity checks; set/reset delays; cancel flights; and display counts and the percentage delayed. Invalid numeric input and blank text are rejected.

This is a single-day classroom scheduler. Data is held in memory and resets when the program restarts. Gates are labels; gate conflicts, real-time operations, persistence, inheritance, recursion, collections, streams, and exception handling are outside this version's scope. Fares and seats appear only in the individual learning examples.

## Example interaction

```text
Choose: 4
Flight number: SK101
Total delay in minutes (0 resets): 35
Delay updated.
SK101 | Delhi | Gate 1 | Scheduled 09:00 | Expected 09:35 | Delayed

Choose: 6
Total: 3, On time: 2, Delayed: 1, Cancelled: 0
Delayed flights: 33.3% of all flights
```

## Practice trace

For departure hour 23, minute 50, and delay 30:

1. Scheduled minutes = `23 * 60 + 50` = 1430.
2. Expected minutes = `1430 + 30` = 1460.
3. Day offset = `1460 / 1440` = 1 (integer division).
4. Hour = `(1460 % 1440) / 60` = 0.
5. Minute = `1460 % 60` = 20.
6. Displayed expected departure = `00:20 (+1 day)`.
