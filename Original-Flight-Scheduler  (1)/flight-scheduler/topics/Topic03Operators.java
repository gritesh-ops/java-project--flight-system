public class Topic03Operators {
    public static void main(String[] args) {
        int seats = 180;
        int booked = 125;
        int available = seats - booked;
        boolean canBook = available > 0 && booked < seats;
        booked++;
        System.out.println("Available before booking: " + available);
        System.out.println("Booking allowed: " + canBook);
        System.out.println("Booked after one booking: " + booked);
    }
}

