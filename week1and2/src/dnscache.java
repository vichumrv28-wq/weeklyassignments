import java.util.*;

class DnsMain {

    static HashMap<String, Integer> pageViews = new HashMap<>();
    static HashMap<String, HashSet<String>> uniqueVisitors = new HashMap<>();
    static HashMap<String, Integer> trafficSources = new HashMap<>();

    public static void main(String[] args) {

        processEvent("/article/breaking-news", "user123", "google");
        processEvent("/article/breaking-news", "user456", "facebook");
        processEvent("/sports/championship", "user789", "direct");
        processEvent("/article/breaking-news", "user999", "google");

        showDashboard();
    }

    static void processEvent(String url, String userId, String source) {

        // Count page views
        pageViews.put(url, pageViews.getOrDefault(url, 0) + 1);

        // Track unique visitors
        if (!uniqueVisitors.containsKey(url)) {
            uniqueVisitors.put(url, new HashSet<>());
        }
        uniqueVisitors.get(url).add(userId);

        // Track traffic sources
        trafficSources.put(source, trafficSources.getOrDefault(source, 0) + 1);
    }

    static void showDashboard() {

        System.out.println("Top Pages:");

        List<Map.Entry<String, Integer>> pages =
                new ArrayList<>(pageViews.entrySet());

        pages.sort((a, b) -> b.getValue() - a.getValue());

        int rank = 1;

        for (Map.Entry<String, Integer> entry : pages) {

            if (rank > 10) break;

            String url = entry.getKey();
            int views = entry.getValue();
            int unique = uniqueVisitors.get(url).size();

            System.out.println(rank + ". " + url +
                    " - " + views + " views (" + unique + " unique)");

            rank++;
        }

        System.out.println("\nTraffic Sources:");

        int total = 0;
        for (int count : trafficSources.values()) {
            total += count;
        }

        for (String source : trafficSources.keySet()) {

            int count = trafficSources.get(source);
            double percent = (count * 100.0) / total;

            System.out.println(source + ": " +
                    String.format("%.2f", percent) + "%");
        }
    }
}