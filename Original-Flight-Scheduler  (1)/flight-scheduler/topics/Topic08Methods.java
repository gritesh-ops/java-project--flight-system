public class Topic08Methods {
    static int calculateDeparture(int scheduled, int delay) {
        return scheduled + delay;
    }

    static void printDeparture(int minutes) {
        System.out.printf("Departure: %02d:%02d%n", minutes / 60, minutes % 60);
    }

    public static void main(String[] args) {
        int expected = calculateDeparture(600, 35);
        printDeparture(expected);
    }
}
