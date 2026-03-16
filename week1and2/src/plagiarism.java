import java.util.*;

class PlagiarismDetector {

    // n-gram size
    private int n;

    // ngram -> set of documents
    private Map<String, Set<String>> ngramIndex;

    // documentId -> list of ngrams
    private Map<String, List<String>> documentNgrams;

    public PlagiarismDetector(int n) {
        this.n = n;
        ngramIndex = new HashMap<>();
        documentNgrams = new HashMap<>();
    }

    // Add document to database
    public void addDocument(String docId, String text) {

        List<String> ngrams = generateNgrams(text);
        documentNgrams.put(docId, ngrams);

        for (String gram : ngrams) {
            ngramIndex
                    .computeIfAbsent(gram, k -> new HashSet<>())
                    .add(docId);
        }
    }

    // Generate n-grams
    private List<String> generateNgrams(String text) {

        List<String> grams = new ArrayList<>();
        String[] words = text.toLowerCase().split("\\s+");

        for (int i = 0; i <= words.length - n; i++) {

            StringBuilder sb = new StringBuilder();

            for (int j = 0; j < n; j++) {
                sb.append(words[i + j]).append(" ");
            }

            grams.add(sb.toString().trim());
        }

        return grams;
    }

    // Analyze a new document
    public void analyzeDocument(String docId, String text) {

        List<String> grams = generateNgrams(text);
        System.out.println("Extracted " + grams.size() + " n-grams");

        Map<String, Integer> matchCount = new HashMap<>();

        for (String gram : grams) {

            if (ngramIndex.containsKey(gram)) {

                for (String existingDoc : ngramIndex.get(gram)) {
                    matchCount.put(
                            existingDoc,
                            matchCount.getOrDefault(existingDoc, 0) + 1
                    );
                }
            }
        }

        // Calculate similarity
        for (String doc : matchCount.keySet()) {

            int matches = matchCount.get(doc);
            int total = grams.size();

            double similarity = (matches * 100.0) / total;

            System.out.println(
                    "Found " + matches + " matching n-grams with \"" + doc + "\""
            );

            System.out.println(
                    "Similarity: " + String.format("%.2f", similarity) + "%"
            );

            if (similarity > 60) {
                System.out.println("PLAGIARISM DETECTED");
            }

            System.out.println();
        }
    }
}

class Main {

    public static void main(String[] args) {

        PlagiarismDetector detector = new PlagiarismDetector(5);

        detector.addDocument(
                "essay_089.txt",
                "machine learning is a method of data analysis that automates analytical model building"
        );

        detector.addDocument(
                "essay_092.txt",
                "machine learning is a method of data analysis that automates analytical model building and artificial intelligence"
        );

        detector.analyzeDocument(
                "essay_123.txt",
                "machine learning is a method of data analysis that automates analytical model building"
        );
    }
}