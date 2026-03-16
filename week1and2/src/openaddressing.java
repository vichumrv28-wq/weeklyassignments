import java.util.*;

class openaddressingMain {

    static int SIZE = 500;

    static String[] spots = new String[SIZE]; // license plate
    static long[] entryTime = new long[SIZE]; // entry time

    static int occupied = 0;
    static int totalProbes = 0;
    static int totalParks = 0;

    // Hash function
    static int hash(String plate) {
        return Math.abs(plate.hashCode()) % SIZE;
    }

    // Park vehicle using linear probing
    static void parkVehicle(String plate) {

        int index = hash(plate);
        int probes = 0;

        while (spots[index] != null) {
            index = (index + 1) % SIZE;
            probes++;
        }

        spots[index] = plate;
        entryTime[index] = System.currentTimeMillis();

        occupied++;
        totalProbes += probes;
        totalParks++;

        System.out.println("Vehicle " + plate +
                " parked at spot #" + index +
                " (" + probes + " probes)");
    }

    // Exit vehicle
    static void exitVehicle(String plate) {

        int index = hash(plate);

        while (spots[index] != null) {

            if (spots[index].equals(plate)) {

                long durationMs = System.currentTimeMillis() - entryTime[index];
                double hours = durationMs / 3600000.0;

                double fee = hours * 5; // $5 per hour

                spots[index] = null;
                entryTime[index] = 0;

                occupied--;

                System.out.println("Vehicle " + plate +
                        " exited from spot #" + index +
                        ", Duration: " + String.format("%.2f", hours) +
                        " hours, Fee: $" + String.format("%.2f", fee));

                return;
            }

            index = (index + 1) % SIZE;
        }

        System.out.println("Vehicle not found.");
    }

    // Find nearest free spot from entrance
    static void findNearestSpot() {

        for (int i = 0; i < SIZE; i++) {
            if (spots[i] == null) {
                System.out.println("Nearest available spot: #" + i);
                return;
            }
        }

        System.out.println("Parking full.");
    }

    // Show statistics
    static void getStatistics() {

        double occupancy = (occupied * 100.0) / SIZE;
        double avgProbes = totalParks == 0 ? 0 : (double) totalProbes / totalParks;

        System.out.println("Occupancy: " + String.format("%.2f", occupancy) + "%");
        System.out.println("Average Probes: " + String.format("%.2f", avgProbes));
    }

    public static void main(String[] args) {

        parkVehicle("ABC-1234");
        parkVehicle("ABC-1235");
        parkVehicle("XYZ-9999");

        findNearestSpot();

        exitVehicle("ABC-1234");

        getStatistics();
    }
}