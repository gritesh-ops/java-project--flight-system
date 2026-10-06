import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

/** Optional development checks; not part of the runnable application JAR. */
public class FlightSchedulerTests {
    private static int checks;

    private static void check(boolean condition, String description) {
        if (!condition) {
            throw new AssertionError(description);
        }
        checks++;
        System.out.println("PASS " + description);
    }

    private static Flight flight(String number, String time, String gate) {
        int minutes = Flight.parseTime(time);
        return new Flight(number, "Demo Air", "Hyderabad", "Delhi",
                minutes, minutes, gate, Flight.SCHEDULED);
    }

    public static void main(String[] args) throws IOException {
        timeAndValidation();
        schedulingAndUpdates();
        sortingAndCapacity();
        persistence();
        System.out.println("ALL " + checks + " CHECKS PASSED");
    }

    private static void timeAndValidation() {
        check(Flight.parseTime("00:00") == 0, "midnight parses correctly");
        check(Flight.parseTime("23:59") == 1439, "last minute parses correctly");
        check(Flight.formatTime(615).equals("10:15"), "minutes format as HH:mm");
        String[] invalid = { "24:00", "09:60", "9:00", "abc", "-1:00", "" };
        for (String text : invalid) {
            boolean rejected = false;
            try { Flight.parseTime(text); }
            catch (IllegalArgumentException expected) { rejected = true; }
            check(rejected, "invalid time rejected: " + text);
        }
        check(flight(" ab12 ", "09:00", "a1").getNumber().equals("AB12"), "flight number normalized");
        check(flight("AB12", "09:00", "a1").getGate().equals("A1"), "gate normalized");
        boolean rejected = false;
        try { new Flight("AA12", "Bad|Air", "HYD", "DEL", 100, 100, "A1", Flight.SCHEDULED); }
        catch (IllegalArgumentException expected) { rejected = true; }
        check(rejected, "record delimiter rejected in text field");
        rejected = false;
        try { new Flight("AA12", "Air", "HYD", "hyd", 100, 100, "A1", Flight.SCHEDULED); }
        catch (IllegalArgumentException expected) { rejected = true; }
        check(rejected, "identical origin and destination rejected");
    }

    private static void schedulingAndUpdates() {
        FlightScheduler scheduler = new FlightScheduler();
        check(scheduler.findFlight("NO1") == null, "missing flight returns null");
        scheduler.addFlight(flight("AB10", "09:00", "A1"));
        scheduler.addFlight(flight("AB20", "09:30", "A1"));
        check(scheduler.size() == 2, "exactly 30 minute gate gap accepted");
        check(scheduler.findFlight(" ab10 ") != null, "search ignores case and surrounding spaces");
        boolean rejected = false;
        try { scheduler.addFlight(flight("AB30", "09:29", "A1")); }
        catch (IllegalArgumentException expected) { rejected = true; }
        check(rejected && scheduler.size() == 2, "29 minute gate gap rejected without adding flight");
        scheduler.addFlight(flight("AB40", "09:00", "B1"));
        check(scheduler.size() == 3, "same time at a different gate accepted");
        rejected = false;
        try { scheduler.addFlight(flight("ab10", "15:00", "C1")); }
        catch (IllegalArgumentException expected) { rejected = true; }
        check(rejected && scheduler.size() == 3, "duplicate flight number rejected");
        rejected = false;
        try { scheduler.setDelay("AB10", 15); }
        catch (IllegalArgumentException expected) { rejected = true; }
        check(rejected && scheduler.findFlight("AB10").getDelayMinutes() == 0,
                "delay clash rejected without changing expected time");
        check(scheduler.findFlight("AB10").getStatus().equals(Flight.SCHEDULED),
                "rejected delay leaves status unchanged");
        rejected = false;
        try { scheduler.changeGate("AB40", "A1"); }
        catch (IllegalArgumentException expected) { rejected = true; }
        check(rejected && scheduler.findFlight("AB40").getGate().equals("B1"),
                "gate change clash rejected without changing gate");
        scheduler.changeStatus("AB20", Flight.CANCELLED);
        scheduler.setDelay("AB10", 15);
        check(scheduler.findFlight("AB10").getExpectedMinutes() == 555,
                "cancelled flight releases gate for a delay");
        scheduler.setDelay("AB10", 10);
        check(scheduler.findFlight("AB10").getDelayMinutes() == 10,
                "delay is total after scheduled time and not cumulative");
        rejected = false;
        try { scheduler.changeStatus("AB20", Flight.SCHEDULED); }
        catch (IllegalArgumentException expected) { rejected = true; }
        check(rejected && scheduler.findFlight("AB20").getStatus().equals(Flight.CANCELLED),
                "reactivating cancelled flight checks gate and preserves state on failure");
        scheduler.setDelay("AB10", 0);
        check(scheduler.findFlight("AB10").getStatus().equals(Flight.SCHEDULED), "zero delay resets status");
        scheduler.changeStatus("AB20", Flight.DEPARTED);
        scheduler.addFlight(flight("AB50", "09:45", "A1"));
        check(scheduler.size() == 4, "departed flight releases gate");
        rejected = false;
        try { scheduler.setDelay("AB20", 10); }
        catch (IllegalArgumentException expected) { rejected = true; }
        check(rejected, "departed flight cannot be delayed");
        rejected = false;
        try { scheduler.changeStatus("AB40", "UNKNOWN"); }
        catch (IllegalArgumentException expected) { rejected = true; }
        check(rejected && scheduler.findFlight("AB40").getStatus().equals(Flight.SCHEDULED),
                "unknown status rejected without changing flight");
        rejected = false;
        try { scheduler.changeStatus("AB40", Flight.DELAYED); }
        catch (IllegalArgumentException expected) { rejected = true; }
        check(rejected, "DELAYED requires positive delay");
        scheduler.setDelay("AB40", 5);
        rejected = false;
        try { scheduler.changeStatus("AB40", Flight.SCHEDULED); }
        catch (IllegalArgumentException expected) { rejected = true; }
        check(rejected, "SCHEDULED requires zero delay");
        FlightScheduler late = new FlightScheduler();
        late.addFlight(flight("LT1", "23:50", "A1"));
        rejected = false;
        try { late.setDelay("LT1", 10); }
        catch (IllegalArgumentException expected) { rejected = true; }
        check(rejected && late.findFlight("LT1").getDelayMinutes() == 0, "cross midnight delay rejected");
        late.setDelay("LT1", 9);
        check(late.findFlight("LT1").getExpectedMinutes() == 1439, "delay up to 23:59 accepted");
    }

    private static void sortingAndCapacity() {
        FlightScheduler scheduler = new FlightScheduler();
        scheduler.addFlight(flight("ZZ1", "12:00", "A1"));
        scheduler.addFlight(flight("BB1", "09:00", "B1"));
        scheduler.addFlight(flight("AA1", "09:00", "C1"));
        Flight[] board = scheduler.getSortedFlights();
        check(board[0].getNumber().equals("AA1") && board[1].getNumber().equals("BB1")
                && board[2].getNumber().equals("ZZ1"), "board sorts by expected time then flight number");
        board[0] = null;
        check(scheduler.getSortedFlights()[0] != null, "replacing snapshot array element does not change scheduler array");
        FlightScheduler full = new FlightScheduler();
        for (int i = 0; i < FlightScheduler.MAX_FLIGHTS; i++) {
            full.addFlight(new Flight("F" + i, "Air", "HYD", "DEL", 500, 500, "A1", Flight.DEPARTED));
        }
        boolean rejected = false;
        try { full.addFlight(flight("OVER1", "10:00", "B1")); }
        catch (IllegalArgumentException expected) { rejected = true; }
        check(rejected && full.size() == 200, "201st flight rejected at array capacity");
    }

    private static void persistence() throws IOException {
        Path directory = Files.createTempDirectory("airport-java-test-");
        Path file = directory.resolve("flights.txt");
        Path malformed = directory.resolve("malformed.txt");
        Path duplicate = directory.resolve("duplicate.txt");
        try {
            check(FlightFileStore.load(file).size() == 0, "missing file loads an empty board");
            FlightScheduler scheduler = new FlightScheduler();
            scheduler.addFlight(flight("IO1", "10:00", "A1"));
            scheduler.setDelay("IO1", 15);
            FlightFileStore.save(file, scheduler);
            FlightScheduler restored = FlightFileStore.load(file);
            check(restored.size() == 1 && restored.findFlight("IO1").getDelayMinutes() == 15
                    && restored.findFlight("IO1").getStatus().equals(Flight.DELAYED),
                    "save and reload preserves flight state");
            restored.changeGate("IO1", "B2");
            FlightFileStore.save(file, restored);
            check(FlightFileStore.load(file).findFlight("IO1").getGate().equals("B2"),
                    "saving an existing file replaces its snapshot");
            String header = "number|airline|origin|destination|scheduled|expected|gate|status";
            Files.write(malformed, Arrays.asList(header,
                    "OK1|Air|HYD|DEL|10:00|10:00|A1|SCHEDULED", "BAD|TOO|FEW"), StandardCharsets.UTF_8);
            byte[] original = Files.readAllBytes(malformed);
            boolean rejected = false;
            try { FlightFileStore.load(malformed); }
            catch (IOException expected) { rejected = expected.getMessage().contains("line 3"); }
            check(rejected, "malformed file reports the exact record line");
            check(Arrays.equals(original, Files.readAllBytes(malformed)), "malformed source file remains unchanged");
            Files.write(duplicate, Arrays.asList(header,
                    "D1|Air|HYD|DEL|10:00|10:00|A1|SCHEDULED",
                    "D1|Air|HYD|DEL|11:00|11:00|B1|SCHEDULED"), StandardCharsets.UTF_8);
            rejected = false;
            try { FlightFileStore.load(duplicate); }
            catch (IOException expected) { rejected = expected.getMessage().contains("Duplicate"); }
            check(rejected, "duplicate IDs rejected during load");
            Files.write(duplicate, Arrays.asList(header,
                    "D1|Air|HYD|DEL|10:00|10:00|A1|SCHEDULED",
                    "D2|Air|HYD|DEL|10:15|10:15|A1|SCHEDULED"), StandardCharsets.UTF_8);
            rejected = false;
            try { FlightFileStore.load(duplicate); }
            catch (IOException expected) { rejected = expected.getMessage().contains("Gate clash"); }
            check(rejected, "gate clashes rejected during load");
            Files.write(duplicate, Arrays.asList("wrong header"), StandardCharsets.UTF_8);
            rejected = false;
            try { FlightFileStore.load(duplicate); }
            catch (IOException expected) { rejected = expected.getMessage().contains("header"); }
            check(rejected, "invalid header rejected");
        } finally {
            Files.deleteIfExists(file);
            Files.deleteIfExists(malformed);
            Files.deleteIfExists(duplicate);
            Files.deleteIfExists(directory);
        }
    }
}
