import java.util.Locale;

/** Module 4: a flight object holds its own validated state. */
public final class Flight {
    public static final String SCHEDULED = "SCHEDULED";
    public static final String DELAYED = "DELAYED";
    public static final String BOARDING = "BOARDING";
    public static final String DEPARTED = "DEPARTED";
    public static final String CANCELLED = "CANCELLED";

    private final String number;
    private final String airline;
    private final String origin;
    private final String destination;
    private final int scheduledMinutes;
    private int expectedMinutes;
    private String gate;
    private String status;

    // A parameterized constructor ensures every object starts with valid data.
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

    private static void validateMinutes(int minutes) {
        if (minutes < 0 || minutes >= 24 * 60) {
            throw new IllegalArgumentException("Time must be within this day, 00:00 to 23:59.");
        }
    }

    public static String normalizeGate(String gate) {
        String normalized = cleanText(gate, "Gate").toUpperCase(Locale.ROOT);
        if (!normalized.matches("[A-Z][0-9]{1,2}")) {
            throw new IllegalArgumentException("Use a gate such as A1, B2 or C12.");
        }
        return normalized;
    }

    public static boolean isKnownStatus(String status) {
        return SCHEDULED.equals(status) || DELAYED.equals(status) || BOARDING.equals(status)
                || DEPARTED.equals(status) || CANCELLED.equals(status);
    }

    public static boolean isActiveStatus(String status) {
        return SCHEDULED.equals(status) || DELAYED.equals(status) || BOARDING.equals(status);
    }

    // Package access: normal changes go through FlightScheduler for gate checks.
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

    /** Module 1 and 5: parse HH:mm and convert it to minutes since midnight. */
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

    @Override
    public String toString() {
        return number + " | " + airline + " | " + origin + " -> " + destination
                + " | Scheduled " + formatTime(scheduledMinutes)
                + " | Expected " + formatTime(expectedMinutes)
                + " | Gate " + gate + " | " + status;
    }
}
