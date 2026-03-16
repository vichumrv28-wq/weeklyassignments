import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

class UsernameSystem {

    // username -> userId
    private ConcurrentHashMap<String, Integer> usernameMap;

    // username -> attempt count
    private ConcurrentHashMap<String, Integer> attemptCount;

    public UsernameSystem() {
        usernameMap = new ConcurrentHashMap<>();
        attemptCount = new ConcurrentHashMap<>();
    }

    // Check username availability
    public boolean checkAvailability(String username) {

        // track attempt frequency
        attemptCount.put(username, attemptCount.getOrDefault(username, 0) + 1);

        // O(1) lookup
        return !usernameMap.containsKey(username);
    }

    // Register new user
    public boolean registerUser(String username, int userId) {

        if (checkAvailability(username)) {
            usernameMap.put(username, userId);
            return true;
        }

        return false;
    }

    // Suggest alternative usernames
    public List<String> suggestAlternatives(String username) {

        List<String> suggestions = new ArrayList<>();

        // append numbers
        for (int i = 1; i <= 5; i++) {
            String candidate = username + i;

            if (!usernameMap.containsKey(candidate)) {
                suggestions.add(candidate);
            }
        }

        // replace "_" with "."
        String modified = username.replace("_", ".");
        if (!usernameMap.containsKey(modified)) {
            suggestions.add(modified);
        }

        return suggestions;
    }

    // Find most attempted username
    public String getMostAttempted() {

        String mostAttempted = null;
        int max = 0;

        for (Map.Entry<String, Integer> entry : attemptCount.entrySet()) {

            if (entry.getValue() > max) {
                max = entry.getValue();
                mostAttempted = entry.getKey();
            }
        }

        return mostAttempted + " (" + max + " attempts)";
    }

    // Demo
    public static void main(String[] args) {

        UsernameSystem system = new UsernameSystem();

        system.registerUser("john_doe", 101);
        system.registerUser("alice", 102);

        System.out.println(system.checkAvailability("john_doe")); // false
        System.out.println(system.checkAvailability("jane_smith")); // true

        System.out.println(system.suggestAlternatives("john_doe"));

        system.checkAvailability("admin");
        system.checkAvailability("admin");
        system.checkAvailability("admin");

        system.checkAvailability("guest");
        system.checkAvailability("guest");

        System.out.println(system.getMostAttempted());
    }
}