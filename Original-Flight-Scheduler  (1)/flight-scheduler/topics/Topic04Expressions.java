public class Topic04Expressions {
    public static void main(String[] args) {
        int hour = 10;
        int minute = 25;
        int delay = 40;
        int expected = hour * 60 + minute + delay;
        double occupancy = 125 * 100.0 / 180;
        System.out.printf("Expected departure: %02d:%02d%n", expected / 60, expected % 60);
        System.out.printf("Occupancy: %.1f%%%n", occupancy);
    }
}

