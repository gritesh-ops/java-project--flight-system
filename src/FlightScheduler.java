import java.util.Arrays;
import java.util.Locale;

/** Module 3: a 1D array, linear search, traversal and modular methods. */
public class FlightScheduler {
    public static final int MAX_FLIGHTS = 200;
    public static final int GATE_GAP_MINUTES = 30;
    private final Flight[] flights = new Flight[MAX_FLIGHTS];
    private int count = 0;

    public int size() { return count; }

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

    private Flight requireFlight(String number) {
        Flight found = findFlight(number);
        if (found == null) {
            throw new IllegalArgumentException("Flight not found: " + number);
        }
        return found;
    }

    /** Same gate plus less than 30 minutes between active flights is a clash. */
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

    /** Delay is the TOTAL minutes after the scheduled time, not an increment. */
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

    public void changeGate(String number, String gate) {
        Flight flight = requireFlight(number);
        if (!flight.isActive()) {
            throw new IllegalArgumentException("Cannot change the gate of a departed or cancelled flight.");
        }
        String checkedGate = Flight.normalizeGate(gate);
        checkGate(checkedGate, flight.getExpectedMinutes(), flight.getStatus(), flight.getNumber());
        flight.updateBoardDetails(flight.getExpectedMinutes(), checkedGate, flight.getStatus());
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

    public Flight[] getSortedFlights() {
        Flight[] board = Arrays.copyOf(flights, count);
        Arrays.sort(board, new FlightTimeComparator());
        return board;
    }
}
