import java.util.*;

class RateLimiterMain {

    // Token bucket structure
    static class TokenBucket {
        int tokens;
        int maxTokens;
        double refillRate; // tokens per second
        long lastRefillTime;

        TokenBucket(int maxTokens, double refillRate) {
            this.maxTokens = maxTokens;
            this.tokens = maxTokens;
            this.refillRate = refillRate;
            this.lastRefillTime = System.currentTimeMillis();
        }

        // Refill tokens based on time passed
        void refill() {
            long now = System.currentTimeMillis();
            double seconds = (now - lastRefillTime) / 1000.0;
            int refill = (int) (seconds * refillRate);

            if (refill > 0) {
                tokens = Math.min(maxTokens, tokens + refill);
                lastRefillTime = now;
            }
        }
    }

    // clientId -> token bucket
    static HashMap<String, TokenBucket> clients = new HashMap<>();

    static final int MAX_REQUESTS = 1000;
    static final double REFILL_RATE = 1000.0 / 3600.0; // 1000 per hour

    // Check rate limit
    static void checkRateLimit(String clientId) {

        TokenBucket bucket = clients.get(clientId);

        if (bucket == null) {
            bucket = new TokenBucket(MAX_REQUESTS, REFILL_RATE);
            clients.put(clientId, bucket);
        }

        bucket.refill();

        if (bucket.tokens > 0) {
            bucket.tokens--;
            System.out.println("Allowed (" + bucket.tokens + " requests remaining)");
        } else {
            System.out.println("Denied (Rate limit exceeded)");
        }
    }

    // Show status
    static void getRateLimitStatus(String clientId) {

        TokenBucket bucket = clients.get(clientId);

        if (bucket == null) {
            System.out.println("No requests made yet.");
            return;
        }

        int used = bucket.maxTokens - bucket.tokens;

        System.out.println("Used: " + used +
                ", Limit: " + bucket.maxTokens +
                ", Remaining: " + bucket.tokens);
    }

    public static void main(String[] args) {

        checkRateLimit("abc123");
        checkRateLimit("abc123");
        checkRateLimit("abc123");

        getRateLimitStatus("abc123");
    }
}