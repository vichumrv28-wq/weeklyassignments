import java.util.*;

class multilevelMain {

    static int L1_CAPACITY = 10000;
    static int L2_CAPACITY = 100000;

    // L1 cache (memory) with LRU
    static LinkedHashMap<String, String> L1 =
            new LinkedHashMap<String, String>(L1_CAPACITY, 0.75f, true) {
                protected boolean removeEldestEntry(Map.Entry<String,String> e) {
                    return size() > L1_CAPACITY;
                }
            };

    // L2 cache (SSD simulated)
    static LinkedHashMap<String, String> L2 =
            new LinkedHashMap<String, String>(L2_CAPACITY, 0.75f, true) {
                protected boolean removeEldestEntry(Map.Entry<String,String> e) {
                    return size() > L2_CAPACITY;
                }
            };

    // L3 database (simulated)
    static HashMap<String, String> database = new HashMap<>();

    // statistics
    static int L1Hits = 0;
    static int L2Hits = 0;
    static int L3Hits = 0;

    // get video
    static String getVideo(String videoId) {

        // L1 check
        if (L1.containsKey(videoId)) {
            L1Hits++;
            System.out.println("L1 Cache HIT");
            return L1.get(videoId);
        }

        // L2 check
        if (L2.containsKey(videoId)) {
            L2Hits++;
            System.out.println("L2 Cache HIT → Promoted to L1");

            String video = L2.get(videoId);
            L1.put(videoId, video);
            return video;
        }

        // L3 database
        if (database.containsKey(videoId)) {
            L3Hits++;
            System.out.println("L3 Database HIT → Added to L2");

            String video = database.get(videoId);
            L2.put(videoId, video);
            return video;
        }

        System.out.println("Video not found.");
        return null;
    }

    // add video to database
    static void addVideo(String id, String data) {
        database.put(id, data);
    }

    // invalidate cache (content updated)
    static void invalidate(String id) {
        L1.remove(id);
        L2.remove(id);
        System.out.println("Cache invalidated for " + id);
    }

    // statistics
    static void getStatistics() {

        int total = L1Hits + L2Hits + L3Hits;

        System.out.println("\nCache Statistics");

        if (total == 0) {
            System.out.println("No requests yet.");
            return;
        }

        System.out.println("L1 Hit Rate: " + (L1Hits * 100.0 / total) + "%");
        System.out.println("L2 Hit Rate: " + (L2Hits * 100.0 / total) + "%");
        System.out.println("L3 Hit Rate: " + (L3Hits * 100.0 / total) + "%");
    }

    public static void main(String[] args) {

        addVideo("video_123", "Movie A");
        addVideo("video_999", "Movie B");

        getVideo("video_123"); // DB → L2
        getVideo("video_123"); // L2 → L1
        getVideo("video_123"); // L1

        getVideo("video_999"); // DB → L2

        invalidate("video_123");

        getStatistics();
    }
}