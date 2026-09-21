public class Topic10Arrays {
    public static void main(String[] args) {
        String[] numbers = {"SK101", "SK202", "SK303"};
        String wanted = "SK202";
        int found = -1;
        for (int i = 0; i < numbers.length; i++) {
            System.out.println("Index " + i + ": " + numbers[i]);
            if (numbers[i].equals(wanted)) {
                found = i;
            }
        }
        System.out.println("Matching index: " + found);
    }
}

