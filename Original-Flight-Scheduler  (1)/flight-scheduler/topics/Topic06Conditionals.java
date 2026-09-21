public class Topic06Conditionals {
    public static void main(String[] args) {
        boolean cancelled = false;
        int delay = 25;
        if (cancelled) {
            System.out.println("Cancelled");
        } else if (delay > 0) {
            System.out.println("Delayed");
        } else {
            System.out.println("On time");
        }
        int choice = 1;
        switch (choice) {
            case 1: System.out.println("Display board selected"); break;
            case 2: System.out.println("Search selected"); break;
            default: System.out.println("Invalid choice");
        }
    }
}

