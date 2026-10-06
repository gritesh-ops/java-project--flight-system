# Airport Flight Board and Scheduler Module Wise Topics and Viva Guide

This project uses only the Java topics needed for a small console departure board. The tables below cover the topics in all five supplied modules, state whether the application uses them, point to the relevant code, and explain the design choice.

The mapping refers to the six production files in `src`. Optional test code and Java library internals do not count as deliberately implemented project features. For example, calling a library method does not mean the student has designed method overloading or an inheritance hierarchy.

## Module 1 From Problem to Program and Java Foundations

### Selected topics

| Topic | Application location | Why used |
|---|---|---|
| Problem solving | Project design and `FlightScheduler.checkGate` | Convert an operator's gate-clash problem into clear data and rules |
| Algorithms and flowcharts | `PROJECT_EXPLANATION.md` | Explain the menu, flight addition, clash detection and saving before discussing code |
| Java environment | JDK compilation and JRE execution in `README.md` | Compile `.java` files and run the application JAR |
| Java program structure | Imports, class declarations and `AirportApp.main` | Give the program an entry point and organize responsibilities |
| Primitive types | `int` times/counts, `boolean` decisions, `double` average; `charAt` returns a `char` during text checks | Represent numeric values and decisions without unnecessary data types |
| Variables | `count`, `difference`, `expected`, `averageDelay` | Store changing state and intermediate calculations |
| Constants | `MAX_FLIGHTS`, `GATE_GAP_MINUTES`, status strings | Keep shared limits and status names consistent |
| Identifiers and literals | Names such as `scheduledMinutes`; values such as `30`, `0`, `true`, `null`, `"A1"` | Make Java expressions meaningful and readable |
| Arithmetic operators | `+`, `-`, `*`, `/`, `%` in `Flight` and scheduler | Convert HH:mm to minutes, compute delays, format time and calculate averages |
| Relational operators | `<`, `>`, `<=`, `>=`, `==`, `!=` | Validate bounds, compare minutes and test null references |
| Logical operators | `&&`, `\|\|`, `!` | Combine same-gate/time conditions and identify active statuses |
| Assignment operators | `=`, `+=` in flight fields and summary | Assign records and accumulate delay totals |
| Unary operators | `++`, `!`, unary `-` | Advance indices/counts, negate decisions and express the lower gate-gap boundary |
| Special syntax and operators | `new`, `.`, `[]` | Create objects, call members and access array positions |
| Typecasting | `(double) totalDelay / active` in `AirportApp.printSummary` | Avoid integer division and preserve fractional averages |
| Expression evaluation and precedence | `hour * 60 + minute`; grouped gate conditions | Multiplication runs before addition; parentheses make combined conditions explicit |
| Associativity | Expressions such as `24 * 60 - 1 - scheduledMinutes` | Understand that subtraction groups left to right; keep formulas easy to inspect |
| Formatted console output | `System.out.printf`, `String.format` | Align the departure board and print a two-decimal average |
| Typed input using Scanner | `ConsoleInput.readText`, `readInt`, `readTime` | Read lines, then parse and validate the intended type |

### Topics left out

| Topic | Why not used in this application |
|---|---|
| `byte`, `short`, `long`, `float` as project variables | Minutes in a single day and a 200-record count fit in `int`; `double` is suitable for the average |
| Ternary operator `?:` | An explicit `if-else` makes the two delay-status choices easier to explain; ternary would only be a shorter alternative |
| Bitwise operators `&`, `\|`, `^`, `~`, `<<`, `>>`, `>>>` | No binary flags, packed bit fields or device registers are needed |
| `instanceof` | The board holds only `Flight` objects, so runtime subtype checking is unnecessary |
| Complicated precedence puzzles | Business formulas use readable grouping; the project does not need expressions whose main purpose is testing precedence |

`|` inside the file delimiter is text, not a Java bitwise operation. `||` in status validation is a logical operator. An integer comparison with `==` is appropriate; a String value comparison normally uses `equals`.

### Example to explain

```java
int minutes = hour * 60 + minute;
int difference = expected - other.getExpectedMinutes();
double averageDelay = (double) totalDelay / active;
```

09:30 becomes `9 * 60 + 30 = 570`. Fifteen delay minutes shared across four active flights gives `15.0 / 4 = 3.75`. Without the cast, integer division would first produce `3`.

## Module 2 Control Flow Selection and Iteration

### Selected topics

| Topic | Application location | Why used |
|---|---|---|
| `if` | Flight/time/record validation | Reject conditions that violate a rule |
| `if-else` | `FlightScheduler.setDelay`, `AirportApp.findFlight` | Choose delay status or choose found/not-found output |
| Nested `if` | `AirportApp.printSummary` | Count delayed records only after confirming that the flight is active |
| `if-else-if` ladder | Argument selection in `main`, summary categories | Select among more than two alternatives |
| `switch` | Menu in `AirportApp.main` | Select one operation from the numbered menu |
| `default` | Menu switch | Provide a defensive branch if the menu is later changed; validated input normally prevents reaching it |
| `break` | Each menu switch case | Prevent one option from falling through into another operation |
| Compound statements | `{ ... }` blocks | Group validation, loop and case-related statements |
| `while` | Menu, input validation, buffered file reading | Repeat until exit, valid input or end of file |
| Traditional `for` | Array search, gate scan and text control-character check | Use an index when traversing occupied positions |
| `continue` | Gate scan and blank-line handling | Skip an inactive/self record or a blank line |
| Controlled `while (true)` | `ConsoleInput.readInt` and `readTime` | Retry an unknown number of times; a valid value returns and end-of-input throws to the main handler |

### Topics left out

| Topic | Why not used in this application |
|---|---|
| `do-while` | The menu and input loops already express their conditions clearly with `while`; using both forms adds no requirement |
| Nested loops | Each proposed flight is checked against the board in one scan; one operation does not require a nested pairwise loop |
| A deliberately endless loop with no exit | The application must allow save and exit; each validation loop has a return or exception exit |

Loading repeatedly calls a method that itself scans records. Its total work can be quadratic, but that does not mean the production source contains a syntactically nested loop. Explain the code structure and its complexity separately.

### Example to explain

```java
if (!other.isActive() || other.getNumber().equals(ignoredNumber)) {
    continue;
}
```

The scan skips a cancelled/departed flight because it no longer reserves a gate. It also skips the flight being updated, so a record is not compared against itself.

## Module 3 Methods Recursion and Arrays

### Selected topics

| Topic | Application location | Why used |
|---|---|---|
| Modular programming | Six classes with separate responsibilities | Keep console work, flight rules, scheduling and file access understandable |
| Method definition and invocation | `addFlight`, `findFlight`, `setDelay`, `load`, `save` | Name operations and reuse their logic |
| Parameters | Flight number, expected time, gate, status, data-file path | Supply an operation with the information it needs |
| Java parameter passing | Primitive arguments and object references passed to methods | Pass values and access the intended flight/scheduler object |
| Return values | `parseTime` returns `int`, `findFlight` returns `Flight`/`null`, `save` helper returns `boolean` | Communicate calculated results and success/failure |
| Enhanced for-each | Board display, file saving and summary | Traverse every record in the occupied-position snapshot |
| One-dimensional arrays | `Flight[] flights`, sorted `Flight[]`, parsed `String[] fields` | Store a bounded board and the fields of a text record |
| Array traversal | Search, clash check, board display | Inspect each relevant record |
| Searching | `FlightScheduler.findFlight` | Locate a flight by its unique number |
| Summation | `totalDelay += flight.getDelayMinutes()` | Add delay minutes for active flights |
| Average | `AirportApp.printSummary` | Describe the overall active-board delay |
| Counting techniques | Active, departed, cancelled and delayed-active counts | Show the board's operational summary |

### Topics left out

| Topic | Why not used in this application |
|---|---|
| Student-defined method overloading | Every operation has one clear signature; extra signatures would be added only to demonstrate overloading |
| Recursion | Records form a flat board and are processed with simple loops; there is no tree, recursive dependency or divide-and-conquer requirement |
| Recursion base case and recursive case | No recursive method exists, so neither case is needed |
| Two-dimensional arrays | A flight is an object with named fields, not a row of numeric matrix elements |
| Matrix arithmetic | Departure times, statuses and gate validation do not require matrix addition or multiplication |

Java is always pass-by-value. Passing a `Flight` copies its reference value. A method can mutate that same referenced object, but reassigning its parameter does not reassign the caller's variable. Do not call this pass-by-reference.

The average's denominator is **all active flights**, including active flights with zero delay. It is not the number of delayed flights. With the initial sample, total active delay is 15 minutes across four active flights, so the average is 3.75 minutes.

## Module 4 Object Oriented Modelling and Exception Handling

### Selected topics

| Topic | Application location | Why used |
|---|---|---|
| Classes as blueprints | `Flight` | Define what every flight record contains and can do |
| Objects as instances | `new Flight(...)` | Represent each scheduled flight independently |
| Fields and methods | Private Flight data and access/validation methods | Keep state together with record-level behavior |
| Object creation using `new` | Flights, scheduler, input helper, comparator | Create the objects needed by the program |
| References and `null` | Array elements and `findFlight` | Represent object relationships and a missing search result |
| Parameterized constructor | `Flight(...)`, `ConsoleInput(...)` | Require valid initial data or the required Scanner reference |
| `this` | Assignments in constructors/mutator | Distinguish an object's field from a same-named parameter |
| Encapsulation and information hiding | Private fields, accessors, controlled mutator | Avoid uncontrolled public field updates |
| Accessors and mutators | `getNumber`, `getExpectedMinutes`, `updateBoardDetails` | Read details and change related mutable fields after validation |
| Instance members | Flight fields and scheduler state | Give each object its own information |
| Static members and methods | Status/limit constants, time parsing, file helpers and `main` | Share constants and operations that do not need per-object state |
| Constants | `static final` shared constants and final identity fields | Avoid changing the meaning of status names and fixed record details |
| `toString` | `Flight.toString`, printed by search | Give a useful full record representation |
| A fundamental interface and is-a relationship | `FlightTimeComparator implements Comparator<Flight>` | A FlightTimeComparator is a Comparator that the sorting library can use |
| Method overriding | `compare` and `toString`, marked `@Override` | Supply interface behavior and replace Object's default text representation |
| Programming to an interface | Comparator contract used by `Arrays.sort` | The library asks for comparison behavior rather than a particular flight-comparator implementation |
| Checked exceptions | `IOException` in file load/save | Require callers to handle or declare storage failures |
| Unchecked exceptions | `IllegalArgumentException`, `NumberFormatException`, `NoSuchElementException` | Report invalid operations/input and end-of-input |
| `try-catch` | Console validation, action handling, file error reporting | Explain failure and preserve usable program control |
| `finally` | `FlightFileStore.save` | Attempt temporary-file cleanup even when a write/move fails |
| `throw` | Validation and record error translation | Raise a precise failure where a rule is violated |
| `throws` | `FlightFileStore.load` and `save` | Declare checked storage failures for the caller to handle |

### Topics left out

| Topic | Why not used in this application |
|---|---|
| Explicit default/no-argument constructors | A usable flight needs its full details; an empty Flight would risk an invalid record. A class such as FlightScheduler has an implicit no-argument constructor, but the project does not design a custom default-constructor feature |
| Constructor overloading | One full Flight constructor is sufficient for loading and adding records |
| Custom single inheritance | There is no required specialized subtype of Flight |
| Custom multilevel inheritance | No chain of flight categories provides necessary behavior |
| Custom hierarchical inheritance | Separate domestic/international subclasses would add structure without differing requirements in this version |
| Multiple class inheritance | Java does not support extending multiple classes; this project does not require combining multiple interface roles either |
| Hybrid inheritance modelling | No mixed hierarchy is required by the flat board |
| A new project-specific interface | The existing Comparator contract already solves the only behavior-plugging need |
| Multi-catch | Each existing handler addresses one exception family or one particular I/O case; a multi-catch does not simplify the required behavior |
| Catching `Error` | Resource/runtime failures such as an out-of-memory Error are not ordinary recoverable input errors; this application does not claim it can safely recover from them |

Every Java class implicitly inherits from `Object` unless it extends another class. `Flight.toString` uses that inherited contract. Therefore, say **no custom class inheritance hierarchy**, rather than saying Java inheritance is entirely absent. `Flight` is final to preserve its validation behavior; no Flight subclass is needed.

A typing mistake such as `09:99` is not a Java `Error`; it is invalid application input. `IllegalArgumentException` is an unchecked exception, `IOException` is checked, and an `Error` is a separate category under `Throwable`.

## Module 5 Strings Library Algorithms and File Input Output

### Selected topics

| Topic | Application location | Why used |
|---|---|---|
| String abstraction and immutability | Names, routes, gates, statuses | Store readable text; operations return a String value rather than editing character storage in place |
| Character representation and indexing | `charAt` in control-character validation | Examine individual characters in a text field |
| Length | Flight-number/text limits and array lengths | Validate text and inspect record field counts |
| Substring | `Flight.parseTime` | Extract hour and minute from HH:mm |
| Concatenation | Messages and `Flight.toString` | Build readable console explanations and a full record description |
| Comparison using `equals` | Duplicate IDs, statuses and gates | Compare String content correctly |
| Comparison ignoring case | Route validation | Prevent the same city from being accepted merely with different case |
| Case conversion | `toUpperCase(Locale.ROOT)` for flight IDs, gates and statuses | Use consistent keys independent of the machine's language setting |
| Searching within text | `contains` in text validation | Reject forbidden pipe/newline characters |
| Ordered String comparison | `compareTo` in comparator | Break equal-time ties by flight number |
| Split | `FlightFileStore.load` using `split` | Tokenize the eight fields of a saved record |
| Join | `String.join` in save | Rebuild the pipe-separated record consistently |
| Trimming | Input and key normalization | Ignore accidental surrounding spaces |
| Validation and simple tokenization | File loader and Flight constructor | Convert text into trusted flight records |
| `Arrays.sort` | `FlightScheduler.getSortedFlights` | Sort a Flight array by the selected Comparator |
| `Comparator` | `FlightTimeComparator` | Define expected-time ordering without changing the Flight class's general meaning |
| `Path` and `Files` | `AirportApp`, `FlightFileStore` | Select files, create directories, read/write text and replace the saved snapshot |
| Buffered readers and writers | File loader/saver | Process records efficiently and clearly |
| Line-by-line processing | `BufferedReader.readLine` | Validate one record at a time with an exact line number |
| Missing files | `Files.notExists` | Allow first use with an empty board |
| Malformed records | Header/field/time/duplicate/clash checks | Stop safely and report the problem without overwriting original records |
| Try-with-resources | Reader, writer and Scanner | Close resources automatically on normal and exceptional paths |

### Topics left out

| Topic | Why not used in this application |
|---|---|
| String content comparison using `==` | `==` compares reference identity for objects; IDs/statuses need content equality, so use `equals`. The code does use `== null` for a reference-absence check |
| `StringBuilder` | This version builds short fixed-size messages and joins record fields; it does not repeatedly append a large text document in a loop |
| `StringBuffer` | No shared mutable string is used by multiple threads; its synchronization is unnecessary |
| Lowercase conversion as a separate operation | Uppercase normalization already handles the keys; converting them both ways would add no feature |
| Whole-file loading via `readAllLines`/`readString` | Line-by-line reading gives precise record errors and avoids needing a whole-file collection. The production app targets Java 8, which does not have `Files.readString` |
| Appending flight updates | A saved snapshot must contain one current record per flight; appending updates would create duplicates without a separate event-log design |
| `Collections.sort` | The storage is a `Flight[]`, not a List, so `Arrays.sort` is the appropriate API |
| `Arrays.binarySearch` | The display is ordered by expected time but lookup is by flight number. Binary search requires compatible ordering; a simple O(n) scan is enough for 200 records |
| A `Flight implements Comparable` design | Expected time is a chosen board order, while another view might order by gate or ID. A Comparator keeps that policy separate |

`String` already has its own comparison behavior; calling `String.compareTo` in a tie breaker does not mean this project implements `Comparable<Flight>`. `Comparator<Flight>` is Java's typed notation for a comparator that compares Flight objects. Generic-type design is not added as a separate project feature.

The small format-validation patterns use standard library `matches`; they are supporting input checks, not a separate advanced regular-expression project. Supporting APIs such as `Locale`, `StandardCharsets`, `Arrays.copyOf`, and file replacement options serve the selected syllabus operations.

## Thirty Viva Questions with Short Answers

1. **What is your project?** It is an offline Java console departure board that records flight times, validates gate separation, displays ordered flights and saves records.
2. **Why did you select only some syllabus topics?** I selected concepts that solve actual requirements and kept unused alternatives out so the implementation is easier to justify.
3. **Why a console application?** The syllabus includes Scanner input and formatted console output. A GUI would add a framework without helping the core scheduling logic.
4. **Why store time as minutes?** Arithmetic and comparisons are simple. For example, 10:15 is 615 minutes after midnight.
5. **What is the difference between scheduled and expected time?** Scheduled is the original departure time. Expected is the current planned time after the total delay.
6. **What is the gate-clash rule?** Two active flights at the same gate must have expected times at least 30 minutes apart. This is our teaching assumption.
7. **What happens at a 30-minute gap?** It is accepted because only a gap below 30 is rejected.
8. **Can two flights use different gates at the same time?** Yes. This version checks gate separation, not runway capacity or all airport resources.
9. **Why do cancelled and departed flights release a gate?** They are no longer active departures needing that gate in this model, but their records remain visible.
10. **What happens if a delay causes a clash?** The scheduler checks the proposed values first, throws an exception and leaves the existing flight unchanged.
11. **Is the delay cumulative?** No. It is the total minutes after scheduled time. Setting 10 minutes after 15 changes the total to 10.
12. **Why use a one-dimensional array?** The board is bounded at 200 records and every element is a Flight object with named fields.
13. **Why no two-dimensional array?** The records contain different kinds of data, and the problem does not involve a numeric matrix.
14. **Which search do you use?** Linear search by normalized flight number, with O(n) time.
15. **Why not binary search?** The display's time order does not match flight-number search order, and n is small.
16. **How is the board sorted?** `Arrays.sort` calls the Comparator. Expected time is the first key and flight number breaks ties.
17. **Comparator or Comparable?** I use Comparator so the display's ordering rule stays separate from the Flight data class. Flight does not implement Comparable.
18. **Where is encapsulation?** Flight fields are private; accessors expose values, and the scheduler uses a controlled mutator after validating a change.
19. **Why a parameterized constructor?** A new Flight must have a valid ID, route, times, gate and status from the start.
20. **What does `this` mean?** It refers to the current object, for example `this.gate = gate` assigns the parameter to its field.
21. **Which members are static?** Shared constants, time helpers, file helpers and main. Each flight's times and gate are instance data.
22. **Where is overriding?** `Flight.toString` overrides Object's text representation, and the comparator's `compare` implements the Comparator method.
23. **Did you use inheritance?** I did not create a custom class hierarchy. Java's Object inheritance exists, and the comparator implements a standard interface.
24. **Is Java pass-by-reference?** No. Java passes values. For objects, the passed value is a copy of the reference.
25. **Why not recursion or overloading?** Iterative scans and one signature per operation meet the requirements without extra call-stack or interface complexity.
26. **Why equals rather than double equals for Strings?** `equals` checks text content; `==` checks whether object references identify the same object.
27. **Checked versus unchecked exception?** IOException is checked and handled/declared. Invalid values raise unchecked exceptions such as IllegalArgumentException.
28. **What do throw and throws do?** `throw` raises an exception at a particular statement; `throws` declares checked failures that may leave a method.
29. **How do you protect file data?** I validate on load, stop on malformed records, write a temporary snapshot, close it, then replace the destination. Try-with-resources closes streams.
30. **What are the project's limits?** Single day, 200 records, one operator, no live data, no dates or runway optimization, and no multi-user locking. Unsaved changes can be lost on forced exit.

## A Short Introduction for the Viva

“My project is an Airport Flight Board and Scheduler written in Java. It maintains a single day's departure records, displays them by expected time, and prevents active flights from using the same gate within a 30-minute gap. I used Scanner input, conditional statements, loops, methods, a one-dimensional array, flight objects, encapsulation, a Comparator, exceptions, Strings and text file operations. I did not add recursion, matrix arithmetic, bitwise operations, a custom inheritance hierarchy or a GUI because the current requirements do not need them.”

## Topics to Demonstrate from the Code

| Open this method | Explain this concept |
|---|---|
| `Flight.parseTime` | Substring, parsing, arithmetic, validation |
| `ConsoleInput.readInt` | Scanner, while, try-catch, return |
| `AirportApp.main` | Program structure, switch, break, loop, exception handling |
| `FlightScheduler.findFlight` | 1D array, linear search, equals, null |
| `FlightScheduler.checkGate` | Logical/relational operators, continue, business rule |
| `FlightScheduler.setDelay` | Parameters, conditions and validation before mutation |
| `Flight` constructor and `updateBoardDetails` | this, fields, object validity, encapsulation |
| `FlightTimeComparator.compare` | Interface, overriding and ordering |
| `AirportApp.printSummary` | Counting, sum, nested if, cast, average |
| `FlightFileStore.load` and `save` | split, join, Files/Path, throws, resources, finally |
