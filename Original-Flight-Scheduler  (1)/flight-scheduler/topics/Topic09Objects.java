public class Topic09Objects {
    String number;
    int gate;

    Topic09Objects(String number, int gate) {
        this.number = number;
        this.gate = gate;
    }

    void display() {
        System.out.println(number + " at gate " + gate);
    }

    public static void main(String[] args) {
        Topic09Objects first = new Topic09Objects("SK101", 2);
        Topic09Objects second = new Topic09Objects("SK202", 5);
        first.display();
        second.display();
    }
}
