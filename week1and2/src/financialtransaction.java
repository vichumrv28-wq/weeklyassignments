import java.util.*;

class financialtransactionMain {

    static class Transaction {
        int id;
        int amount;
        String merchant;
        String account;
        long time;

        Transaction(int id, int amount, String merchant, String account, long time) {
            this.id = id;
            this.amount = amount;
            this.merchant = merchant;
            this.account = account;
            this.time = time;
        }
    }

    // Classic Two Sum
    static void findTwoSum(List<Transaction> list, int target) {

        HashMap<Integer, Transaction> map = new HashMap<>();

        for (Transaction t : list) {

            int complement = target - t.amount;

            if (map.containsKey(complement)) {
                Transaction t2 = map.get(complement);

                System.out.println("Two-Sum Pair: (" + t2.id + ", " + t.id + ")");
            }

            map.put(t.amount, t);
        }
    }

    // Two Sum within 1 hour
    static void findTwoSumWithTime(List<Transaction> list, int target) {

        HashMap<Integer, Transaction> map = new HashMap<>();

        for (Transaction t : list) {

            int complement = target - t.amount;

            if (map.containsKey(complement)) {

                Transaction t2 = map.get(complement);

                if (Math.abs(t.time - t2.time) <= 3600) {
                    System.out.println("Time Window Pair: (" + t2.id + ", " + t.id + ")");
                }
            }

            map.put(t.amount, t);
        }
    }

    // Duplicate detection
    static void detectDuplicates(List<Transaction> list) {

        HashMap<String, List<Transaction>> map = new HashMap<>();

        for (Transaction t : list) {

            String key = t.amount + "_" + t.merchant;

            map.putIfAbsent(key, new ArrayList<>());
            map.get(key).add(t);
        }

        for (String key : map.keySet()) {

            List<Transaction> group = map.get(key);

            if (group.size() > 1) {

                System.out.println("Duplicate transactions for " + key);

                for (Transaction t : group) {
                    System.out.println("Transaction ID: " + t.id +
                            ", Account: " + t.account);
                }
            }
        }
    }

    // K-Sum using recursion
    static void findKSum(List<Transaction> list, int target, int k,
                         int index, List<Integer> current) {

        if (k == 0 && target == 0) {
            System.out.println("K-Sum Match: " + current);
            return;
        }

        if (k == 0 || index >= list.size()) return;

        for (int i = index; i < list.size(); i++) {

            current.add(list.get(i).id);

            findKSum(list,
                    target - list.get(i).amount,
                    k - 1,
                    i + 1,
                    current);

            current.remove(current.size() - 1);
        }
    }

    public static void main(String[] args) {

        List<Transaction> transactions = new ArrayList<>();

        transactions.add(new Transaction(1, 500, "StoreA", "acc1", 1000));
        transactions.add(new Transaction(2, 300, "StoreB", "acc2", 1100));
        transactions.add(new Transaction(3, 200, "StoreC", "acc3", 1200));
        transactions.add(new Transaction(4, 500, "StoreA", "acc4", 1300));

        System.out.println("Two-Sum:");
        findTwoSum(transactions, 500);

        System.out.println("\nTwo-Sum with Time Window:");
        findTwoSumWithTime(transactions, 500);

        System.out.println("\nDuplicate Detection:");
        detectDuplicates(transactions);

        System.out.println("\nK-Sum:");
        findKSum(transactions, 1000, 3, 0, new ArrayList<>());
    }
}