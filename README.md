# Airport Flight Board and Scheduler

A Java console project for managing a single day's airport departure board. It uses selected topics from the five supplied syllabus modules. The project contains original source code, sample records, a runnable JAR, launch scripts, and a topic-by-topic viva guide.

## What the project does

- Adds a flight with its airline, origin, destination, scheduled time and gate.
- Displays flights ordered by expected time and then flight number.
- Searches by flight number, ignoring case.
- Sets or resets a total delay, changes a gate, and updates a status.
- Rejects duplicate IDs and gate clashes before changing the board.
- Shows flight counts and the average delay across all active flights.
- Loads and saves a UTF-8 text file without a database or external dependencies.

This is a classroom departure-board simulation. It does not obtain live airline data, make bookings, allocate runways, or optimize an entire airport timetable.

## Run on Windows

1. Extract the ZIP completely.
2. Open the extracted `Airport_Flight_Board_Scheduler` folder.
3. Double-click `run.bat`. Java 8 or later must be installed.
4. Choose `1` to show the supplied demonstration board.
5. Choose `0` to save and exit.

The prebuilt JAR is included, so compilation is optional. In a terminal opened in the project folder:

```text
java -jar Airport_Flight_Board_Scheduler.jar
```

Run from the project folder so the program finds `data/flights.txt`. The JAR does not contain an editable copy of the data file.

## Compile the source

With JDK 9 or later, run `build.bat` on Windows or `sh build.sh` on Linux/macOS. These scripts target Java 8 bytecode. A compiler may warn that the Java 8 target is obsolete; the target is chosen for compatibility.

If using JDK 8 itself:

```text
mkdir build
javac -encoding UTF-8 -d build src/*.java
jar cfe Airport_Flight_Board_Scheduler.jar AirportApp -C build .
java -jar Airport_Flight_Board_Scheduler.jar
```

On Windows use `src\*.java` if the shell requires Windows separators. An IDE can import the six files under `src` and run `AirportApp.main`. Set the IDE's working directory to the project folder. No Maven, Gradle, database driver, or network connection is required.

## Rules to explain in the viva

| Rule | Meaning |
|---|---|
| Scope | One day, one departure board, local 24-hour time |
| Identity | A flight number is unique on this board |
| Time | Input must be `HH:mm`, from `00:00` to `23:59` |
| Gate | One letter and one or two digits, for example `A1` or `C12` |
| Gate separation | Active flights at the same gate need at least 30 minutes between expected times |
| Active statuses | `SCHEDULED`, `DELAYED`, `BOARDING` |
| Released gate | `DEPARTED` and `CANCELLED` records do not reserve a gate |
| Delay | Total minutes after scheduled time; setting 10 after setting 15 means a 10-minute total delay |
| Delay reset | Zero delay restores expected time to scheduled time and status to `SCHEDULED` |
| Boarding and delay | Setting a delay changes status to `DELAYED` or `SCHEDULED`; set `BOARDING` again when appropriate |
| Status correction | An operator may reactivate a cancelled/departed record; gate and status consistency checks still apply |
| Capacity | 200 records in a fixed one-dimensional array |
| Persistence | Save option `8` or normal exit `0` writes the current snapshot |

The 30-minute gate rule is a chosen teaching assumption, not an asserted airport regulation. Exactly 30 minutes is allowed. Twenty-nine minutes is rejected. Flights at different gates may have the same expected time. Cancelled/departed records remain visible for audit and summary purposes.

Cross-midnight delays are rejected rather than silently wrapping to the next day. There is no date or time-zone conversion. Flight IDs cannot recur on another date because dates are outside this version's scope.

## Source files and responsibilities

| File | Responsibility |
|---|---|
| `src/AirportApp.java` | Menu, operations, formatted board and summary |
| `src/ConsoleInput.java` | Scanner input and retrying invalid numeric/time input |
| `src/Flight.java` | Flight fields, validation, accessors, controlled mutator and text representation |
| `src/FlightScheduler.java` | Array storage, search, gate checks, delay, gate and status updates |
| `src/FlightTimeComparator.java` | Expected-time ordering and flight-number tie breaker |
| `src/FlightFileStore.java` | Buffered loading, text parsing and safe snapshot saving |

The source is separated by responsibility. `docs/MODULE_WISE_TOPICS_AND_VIVA.md` separately maps these files and methods to the syllabus, explaining both selected and omitted topics.

## Data format

The first line is an exact header:

```text
number|airline|origin|destination|scheduled|expected|gate|status
6E101|IndiGo|Hyderabad|Delhi|09:00|09:00|A1|SCHEDULED
```

The file uses `|` as a delimiter, not CSV. Text fields cannot contain pipes, line breaks or control characters. Blank lines are ignored. Data is validated on loading, including duplicate IDs and gate clashes.

A missing file starts an empty board. A malformed record stops startup and identifies its line; the original file is unchanged. Correct it and restart. Other read failures also stop startup. Saving first writes a temporary file, closes it, and then replaces the destination. Atomic replacement is requested; file systems without support use a regular replacement. This is helpful protection, not a full crash-recovery or multi-user transaction system.

To use a separate data file:

```text
java -jar Airport_Flight_Board_Scheduler.jar practice/flights.txt
```

Edits remain in memory until saved. Save before closing a terminal or pressing Ctrl+C. Avoid running two program instances against the same file; there is no file locking.

## Viva and demonstration files

- `docs/MODULE_WISE_TOPICS_AND_VIVA.md`: complete selected/omitted-topic mapping, reasons, code locations and 30 viva questions.
- `docs/PROJECT_EXPLANATION.md`: problem, objectives, design, algorithms, complexity and limitations.
- `docs/DEMO_STEPS.md`: a repeatable demonstration and expected results.
- `docs/VALIDATION.md`: checks performed on the delivered version.
- `docs/REFERENCE.md`: supplied GitHub link and reference-access limitation.
- `tests/FlightSchedulerTests.java`: optional development checks, outside the application JAR.

Run `test.bat` or `sh test.sh` with JDK 9 or later to repeat the tests. They create temporary test records, not changes to the demonstration data.

## Reference

The user supplied [nckumaruoh/pspj-flight-scheduler](https://github.com/nckumaruoh/pspj-flight-scheduler) as a reference. Access was blocked in this session, so its contents could not be reviewed. This package is an original implementation of the user's requested project and syllabus scope. It is not presented as a copy, fork, or verified adaptation of that repository.
