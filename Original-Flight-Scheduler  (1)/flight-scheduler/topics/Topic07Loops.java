public class Topic07Loops {
    public static void main(String[] args) {
        for (int gate = 1; gate <= 3; gate++) {
            System.out.println("Check gate " + gate);
        }
        int minutesLeft = 3;
        while (minutesLeft > 0) {
            System.out.println("Boarding closes in " + minutesLeft + " minutes");
            minutesLeft--;
        }
        int checks = 0;
        do {
            System.out.println("Perform boarding check");
            checks++;
        } while (checks < 2);
    }
}

