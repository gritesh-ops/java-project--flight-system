# Airport Flight Board and Scheduler Demonstration

Use a copy of the supplied sample so demonstrations do not alter the original. In the project folder on Windows:

```powershell
Copy-Item data\flights.txt data\demo.txt
java -jar Airport_Flight_Board_Scheduler.jar data/demo.txt
```

On Linux/macOS use `cp data/flights.txt data/demo.txt` before the same Java command. Reset the copy before repeating the following demonstration.

## Demonstration Sequence

| Step | Input | Expected result | Topic to explain |
|---|---|---|---|
| 1 | `1` | Six flights in expected-time order | Array traversal, formatted printing, Comparator |
| 2 | `3`, then `6e101` | Full details of 6E101 at 09:00 on A1 | Methods, linear search, uppercase normalization, toString |
| 3 | `4`, `6E101`, `30` | Rejected clash with AI202 at 09:45 | Time arithmetic, logical conditions, exception handling |
| 4 | `3`, `6E101` | Still expected at 09:00 and SCHEDULED | Validation before mutation |
| 5 | `4`, `6E101`, `10` | Accepted at 09:10, DELAYED | if-else, fields and controlled updates |
| 6 | `5`, `6E101`, `B1` | Gate changed to B1 | Gate validation and array scan |
| 7 | `6`, `6E101`, `BOARDING` | Status changed to BOARDING, 10-minute delay retained | Status values and method parameters |
| 8 | `2`, `SG707`, `SpiceJet`, `Hyderabad`, `Goa`, `12:00`, `C1` | New flight accepted | Scanner, parameterized constructor, new, array insertion |
| 9 | `7` | Total 7, active 5, departed 1, cancelled 1; total active delay 25; average 5.00 | Counting, summation, double cast |
| 10 | `8` | Seven records saved | Buffered file writing, join, resource handling |
| 11 | `0` | Saved and exited | Switch, loop termination |
| 12 | Restart using the same demo path; search `SG707` | Added flight restored | Persistence and file parsing |

If an operation is rejected because you have already changed the sample, reset `data/demo.txt` from `data/flights.txt` and repeat in order.

## Invalid Input Demonstration

At the menu, type `abc`. The program asks for a whole number. Type `9`. It asks for a number from 0 to 8. When adding a flight, type `25:00` at the time prompt; it explains the valid range and asks again. A duplicate flight number is rejected after the supplied flight details have been collected.

## Expected Initial Summary

```text
Total: 6 | Active: 4 | Departed: 1 | Cancelled: 1
Active flights with delay: 1 | Total active delay: 15 minutes
Average delay across ALL active flights: 3.75 minutes
```

## Demonstrate Missing and Malformed Files

Give a new, nonexistent filename as the command-line argument. The application reports that it is starting an empty board. Add a record and save to create that file.

For malformed data, use a separate copy and change one record's time to `25:00`, keeping the header. Startup reports the line with the problem and stops. The file is not changed. Restore the record to a valid value before restarting.

## Presentation Order

Explain the problem and scope, show the sample board, demonstrate a rejected gate clash, perform a successful update, show the summary and save/reload. Then open the source methods listed at the end of the module-wise viva guide. Refer to omitted topics only when explaining why a simpler selected technique meets the requirement.
