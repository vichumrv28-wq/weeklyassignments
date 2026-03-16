import java.util.*;

class PageViewEvent {
    String url;
    String userId;
    String source;

    public PageViewEvent(String url, String userId, String source) {
        this.url = url;
        this.userId = userId;
        this.source = source;
    }
}

class AnalyticsDashboard {

    // pageUrl -> visit count
    HashMap<String, Integer> pageViews = new HashMap<>();

    // pageUrl -> unique visitors
    HashMap<String, HashSet<String>> uniqueVisitors = new HashMap<>();

    // traffic source -> count
    HashMap<String, Integer> trafficSources = new HashMap<>();


    // Process incoming event
    public void processEvent(PageViewEvent event) {

        // Count page views
        pageViews.put(event.url,
                pageViews.getOrDefault(event.url, 0) + 1);

        // Track unique visitors
        uniqueVisitors
                .computeIfAbsent(event.url, k -> new HashSet<>())
                .add(event.userId);

        // Track traffic source
        trafficSources.put(event.source,
                trafficSources.getOrDefault(event.source, 0) + 1);
    }

    // Display dashboard
    public void getDashboard() {

        System.out.println("Top Pages:");

        // Sort pages by visit count
        List<Map.Entry<String, Integer>> list =
                new ArrayList<>(pageViews.entrySet());

        list.sort((a, b) -> b.getValue() - a.getValue());

        int count = 0;

        for (Map.Entry<String, Integer> entry : list) {

            if (count == 10) break;

            String page = entry.getKey();
            int views = entry.getValue();
            int unique = uniqueVisitors.get(page).size();

            System.out.println(
                    (count + 1) + ". " + page +
                            " - " + views +
                            " views (" + unique + " unique)"
            );

            count++;
        }

        System.out.println("\nTraffic Sources:");

        int total = 0;
        for (int val : trafficSources.values()) {
            total += val;
        }

        for (String source : trafficSources.keySet()) {

            int countSource = trafficSources.get(source);
            double percent = (countSource * 100.0) / total;

            System.out.println(source + ": " +
                    String.format("%.2f", percent) + "%");
        }
    }
}

class analyticMain {

    public static void main(String[] args) {

        AnalyticsDashboard dashboard = new AnalyticsDashboard();

        dashboard.processEvent(
                new PageViewEvent("/article/breaking-news", "user123", "google"));

        dashboard.processEvent(
                new PageViewEvent("/article/breaking-news", "user456", "facebook"));

        dashboard.processEvent(
                new PageViewEvent("/sports/championship", "user789", "direct"));

        dashboard.processEvent(
                new PageViewEvent("/article/breaking-news", "user999", "google"));

        dashboard.getDashboard();
    }
}