import java.util.NoSuchElementException;
import java.util.Scanner;

/** Module 1 and 2: Scanner input, validation and repetition. */
public class ConsoleInput {
    private final Scanner scanner;

    public ConsoleInput(Scanner scanner) {
        this.scanner = scanner;
    }

    public String readText(String prompt) {
        System.out.print(prompt);
        if (!scanner.hasNextLine()) {
            throw new NoSuchElementException("End of console input.");
        }
        return scanner.nextLine().trim();
    }

    public int readInt(String prompt, int minimum, int maximum) {
        while (true) {
            String text = readText(prompt);
            try {
                int value = Integer.parseInt(text);
                if (value >= minimum && value <= maximum) {
                    return value;
                }
                System.out.println("Enter a number from " + minimum + " to " + maximum + ".");
            } catch (NumberFormatException exception) {
                System.out.println("Enter a whole number.");
            }
        }
    }

    public int readTime(String prompt) {
        while (true) {
            String text = readText(prompt);
            try {
                return Flight.parseTime(text);
            } catch (IllegalArgumentException exception) {
                System.out.println(exception.getMessage());
            }
        }
    }
}
