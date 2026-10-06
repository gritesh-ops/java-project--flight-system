import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/** Module 5: pipe-separated text records and buffered file I/O. */
public class FlightFileStore {
    private static final String HEADER = "number|airline|origin|destination|scheduled|expected|gate|status";

    public static FlightScheduler load(Path file) throws IOException {
        FlightScheduler loaded = new FlightScheduler();
        // A missing file means a new, empty board. Other I/O errors are fatal.
        if (Files.notExists(file)) {
            return loaded;
        }
        try (BufferedReader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            if (!HEADER.equals(reader.readLine())) {
                throw new IOException("Invalid file header. Expected: " + HEADER);
            }
            String line;
            int lineNumber = 1;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (line.trim().isEmpty()) {
                    continue;
                }
                try {
                    String[] fields = line.split("\\|", -1);
                    if (fields.length != 8) {
                        throw new IllegalArgumentException("Expected 8 pipe-separated fields.");
                    }
                    Flight flight = new Flight(fields[0], fields[1], fields[2], fields[3],
                            Flight.parseTime(fields[4]), Flight.parseTime(fields[5]), fields[6], fields[7]);
                    loaded.addFlight(flight);
                } catch (IllegalArgumentException exception) {
                    // Abort safely so malformed source data is never overwritten.
                    throw new IOException("Invalid record at line " + lineNumber + ": "
                            + exception.getMessage(), exception);
                }
            }
        }
        return loaded;
    }

    public static void save(Path file, FlightScheduler scheduler) throws IOException {
        Path target = file.toAbsolutePath();
        Files.createDirectories(target.getParent());
        Path temporary = Files.createTempFile(target.getParent(), "flight-board-", ".tmp");
        try {
            try (BufferedWriter writer = Files.newBufferedWriter(temporary, StandardCharsets.UTF_8)) {
                writer.write(HEADER);
                writer.newLine();
                for (Flight flight : scheduler.getSortedFlights()) {
                    String record = String.join("|", flight.getNumber(), flight.getAirline(),
                            flight.getOrigin(), flight.getDestination(),
                            Flight.formatTime(flight.getScheduledMinutes()),
                            Flight.formatTime(flight.getExpectedMinutes()), flight.getGate(), flight.getStatus());
                    writer.write(record);
                    writer.newLine();
                }
            }
            try {
                Files.move(temporary, target, StandardCopyOption.REPLACE_EXISTING,
                        StandardCopyOption.ATOMIC_MOVE);
            } catch (AtomicMoveNotSupportedException exception) {
                // Some file systems do not support atomic replacement.
                Files.move(temporary, target, StandardCopyOption.REPLACE_EXISTING);
            }
        } finally {
            Files.deleteIfExists(temporary);
        }
    }
}
