import java.util.Comparator;

/** Module 4 and 5: an interface gives Arrays.sort a flight ordering rule. */
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
