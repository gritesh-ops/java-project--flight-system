# Validation of the Delivered Project

Validation date: 6 October 2026.

## Build and Runtime

The six production classes compiled with JDK 25 using `--release 8`, targeting Java 8. Compilation with lint checks produced no source-code warnings after the Flight class was finalized. The ordinary build script reports the compiler's Java 8 target obsolescence warnings; those are compatibility-target notices.

The executable JAR was run using the available Java 8 runtime. It loaded the six sample records, displayed the expected board and initial summary, and saved successfully. The JAR contains only production classes, not test classes.

## Scheduling and Persistence Checks

`tests/FlightSchedulerTests.java` completed with **ALL 44 CHECKS PASSED**.

| Coverage | Verified behavior |
|---|---|
| Time handling | 00:00 and 23:59 accepted; malformed times and out-of-range hours/minutes rejected |
| Text handling | Flight ID/gate normalization; forbidden delimiter and identical route rejection |
| Search | Missing record returns null; case and surrounding spaces handled |
| Gate boundary | Exactly 30 minutes accepted; 29 minutes rejected; different gates may share a time |
| Identity | Duplicate flight number rejected |
| Updates | Rejected delay/gate change leaves state unchanged; total delay is not cumulative; zero resets status |
| Status | Cancelled/departed records release gates; reactivation checks clashes; invalid or inconsistent status rejected |
| Single-day boundary | Delay to 23:59 accepted; delay crossing midnight rejected |
| Ordering | Expected time and flight-number tie breaker verified |
| Array | Changing a snapshot array slot does not change scheduler array slots; 201st record rejected |
| Storage | Missing file, save/reload, snapshot replacement, malformed record, exact line reporting, invalid header, duplicates and gate conflicts checked |

The snapshot-array test verifies array-slot independence. It does not claim that the Flight objects are deep-copied.

## Console Demonstration

The packaged JAR was also exercised through redirected console input using a separate sample copy. Checks verified numeric-menu retries, invalid-time retries, full flight search, a rejected 30-minute delay, state preservation after rejection, a valid 10-minute delay, a gate change, a BOARDING status update, adding SG707, summary calculation, explicit save and normal exit.

The resulting summary had 7 total records, 5 active flights and 25 total active delay minutes, for an average of 5.00. Restarting with the saved file restored SG707 and the edited records.

These checks validate the classroom implementation's stated rules. They are not certification of real-airport operations, concurrency, automatic timetable optimization or guaranteed recovery from every possible system failure.
