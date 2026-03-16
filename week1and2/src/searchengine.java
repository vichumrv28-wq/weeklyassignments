import java.util.*;

class searchengineMain {

    // query -> frequency
    static HashMap<String, Integer> queryFrequency = new HashMap<>();

    // Add or update search query
    static void updateFrequency(String query) {
        queryFrequency.put(query, queryFrequency.getOrDefault(query, 0) + 1);
    }

    // Return top 10 suggestions for prefix
    static void search(String prefix) {

        PriorityQueue<Map.Entry<String, Integer>> pq =
                new PriorityQueue<>(Comparator.comparingInt(Map.Entry::getValue));

        for (Map.Entry<String, Integer> entry : queryFrequency.entrySet()) {

            String query = entry.getKey();

            if (query.startsWith(prefix)) {

                pq.offer(entry);

                if (pq.size() > 10) {
                    pq.poll(); // remove lowest frequency
                }
            }
        }

        List<Map.Entry<String, Integer>> results = new ArrayList<>();

        while (!pq.isEmpty()) {
            results.add(pq.poll());
        }

        Collections.reverse(results);

        System.out.println("Suggestions for \"" + prefix + "\":");

        int rank = 1;

        for (Map.Entry<String, Integer> entry : results) {

            System.out.println(rank + ". " + entry.getKey() +
                    " (" + entry.getValue() + " searches)");

            rank++;
        }
    }

    public static void main(String[] args) {

        updateFrequency("java tutorial");
        updateFrequency("javascript");
        updateFrequency("java download");
        updateFrequency("java tutorial");
        updateFrequency("java 21 features");
        updateFrequency("java tutorial");

        search("jav");
    }
}