import java.io.*;
import java.util.*;

public class Main {
    private static List<String> phraseList1;
    private static List<String> phraseList2;
    private static final Map<String, String> phrasePairs = new HashMap<>();

    static void readInputFromFile(String filename) {
        try (FileInputStream fis = new FileInputStream(filename)) {
            readInput(fis);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void readInput(InputStream is) {
        try (Scanner sc = new Scanner(is)) {
            int n = sc.nextInt();
            sc.nextLine();
            phraseList1 = new ArrayList<>(n);
            for (int i = 0; i < n; i++) {
                phraseList1.add(sc.nextLine());
            }
            int m = sc.nextInt();
            sc.nextLine();
            phraseList2 = new ArrayList<>(m);
            for (int i = 0; i < m; i++) {
                phraseList2.add(sc.nextLine());
            }
        }
    }

    private static void process() {
        List<PhrasePair> pairs = new LinkedList<>();
        for (String phrase1 : phraseList1) {
            for (String phrase2 : phraseList2) {
                pairs.add(new PhrasePair(phrase1, phrase2, calculatePhraseSimilarity(phrase1, phrase2)));
            }
        }
        pairs.sort(Comparator.comparingDouble(p -> -p.similarity));
        while (!pairs.isEmpty()) {
            PhrasePair pair = pairs.get(0);
            phrasePairs.put(pair.phrase1, pair.phrase2);
            pairs.removeIf(p -> p.phrase1.equals(pair.phrase1) || p.phrase2.equals(pair.phrase2));
        }
    }

    private static double calculatePhraseSimilarity(String s1, String s2) {
        String[] split1 = s1.split(" ");
        String[] split2 = s2.split(" ");
        if (split1.length > split2.length) {
            String[] temp = split1;
            split1 = split2;
            split2 = temp;
        }
        double similaritySum = 0;
        for (String word1 : split1) {
            double maxSimilarity = 0;
            for (String word2 : split2) {
                maxSimilarity = Math.max(maxSimilarity, calculateWordSimilarity(word1, word2));
            }
            similaritySum += maxSimilarity;
        }
        return similaritySum / split1.length;
    }

    private static double calculateWordSimilarity(String s1, String s2) {
        String longer = s1;
        String shorter = s2;
        if (s1.length() < s2.length()) {
            longer = s2;
            shorter = s1;
        }
        int longerLength = longer.length();
        if (longerLength == 0) {
            return 1;
        }
        return (longerLength - editDistance(longer, shorter)) / (double) longerLength;
    }

    private static int editDistance(String s1, String s2) {
        s1 = s1.toLowerCase();
        s2 = s2.toLowerCase();

        int[] costs = new int[s2.length() + 1];
        for (int i = 0; i <= s1.length(); i++) {
            int lastValue = i;
            for (int j = 0; j <= s2.length(); j++) {
                if (i == 0)
                    costs[j] = j;
                else if (j > 0) {
                    int newValue = costs[j - 1];
                    if (s1.charAt(i - 1) != s2.charAt(j - 1)) {
                        newValue = Math.min(Math.min(newValue, lastValue), costs[j]) + 1;
                    }
                    costs[j - 1] = lastValue;
                    lastValue = newValue;
                }
            }
            if (i > 0) {
                costs[s2.length()] = lastValue;
            }
        }
        return costs[s2.length()];
    }

    static void writeOutputToFile(String filename) {
        try (FileOutputStream fos = new FileOutputStream(filename)) {
            writeOutput(fos);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void writeOutput(OutputStream os) {
        try (PrintWriter out = new PrintWriter(os)) {
            for (String phrase : phraseList1) {
                out.println(phrase + ":" + phrasePairs.getOrDefault(phrase, "?"));
            }
            Set<String> usedPhrases2 = new HashSet<>(phrasePairs.values());
            for (String phrase : phraseList2) {
                if (!usedPhrases2.contains(phrase)) {
                    out.println(phrase + ":?");
                }
            }
        }
    }

    public static void main(String[] args) {
        readInputFromFile("input.txt");
        process();
        writeOutputToFile("output.txt");
    }
    private static class PhrasePair {
        String phrase1;
        String phrase2;
        double similarity;

        public PhrasePair(String phrase1, String phrase2, double similarity) {
            this.phrase1 = phrase1;
            this.phrase2 = phrase2;
            this.similarity = similarity;
        }
    }

}
