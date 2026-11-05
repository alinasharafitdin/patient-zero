import java.util.*;

/**
 * Missions 3 and 4. Find a word in a long text.
 */
public class Marker {

    /**
     * Mission 3a. The prefix function of p. pi[i] = the length of the longest proper prefix of p[0..i] that is
     * also a suffix of p[0..i] ("proper" means shorter than p[0..i] itself).
     * Example: p = "ACGAC" gives [0, 0, 0, 1, 2]. The last number is the "Border length" line.
     */
    public static int[] prefixFunction(String p) {
        int[] pi = new int[p.length()];

        for (int i = 1; i < p.length(); i++) {
            int j = pi[i - 1];

            while (j > 0 && p.charAt(i) != p.charAt(j)) {
                j = pi[j - 1];
            }

            if (p.charAt(i) == p.charAt(j)) {
                j++;
            }

            pi[i] = j;
        }

        return pi;
    }

    /**
     * Mission 3b. KMP. All start positions (counting from 0, in increasing order) where p occurs in text.
     * Overlapping occurrences count: "ACG" occurs in "ACGACG" at 0 and 3, and "AA" in "AAA" at 0 and 1.
     * Use the prefix function: the text pointer never goes back. The number of positions is "Marker hits",
     * the first one is "First hit" (-1 when there is none).
     */
    public static int[] findAll(String text, String p) {
        if (p.length() == 0) {
            return new int[0];
        }

        int[] pi = prefixFunction(p);
        ArrayList<Integer> positions = new ArrayList<>();
        int j = 0;

        for (int i = 0; i < text.length(); i++) {
            while (j > 0 && text.charAt(i) != p.charAt(j)) {
                j = pi[j - 1];
            }

            if (text.charAt(i) == p.charAt(j)) {
                j++;
            }

            if (j == p.length()) {
                positions.add(i - p.length() + 1);
                j = pi[j - 1];
            }
        }

        int[] result = new int[positions.size()];

        for (int i = 0; i < positions.size(); i++) {
            result[i] = positions.get(i);
        }

        return result;
    }
    /**
     * Mission 4. Rabin-Karp. The number of positions i in a such that the word of k letters starting at i
     * (a[i..i+k-1]) also occurs somewhere in b. Count positions, not different words. This is the "Shared words" line.
     * The idea: give every window of k letters a number (a rolling hash, updated in O(1) when the window moves by one),
     * look the number up among the windows of b, and when it is found COMPARE THE LETTERS, because two different words
     * can have the same hash.
     * Example: a = "ACGTAC", b = "TTACGT", k = 4: the words of a are ACGT, CGTA, GTAC; only ACGT is in b, so the answer is 1.
     */
    public static int sharedWords(String a, String b, int k) {
        if (k <= 0 || a.length() < k || b.length() < k) {
            return 0;
        }

        long base = 31;
        long mod = 1_000_000_007;
        long power = 1;

        for (int i = 1; i < k; i++) {
            power = (power * base) % mod;
        }

        HashMap<Long, ArrayList<Integer>> windows = new HashMap<>();
        long hashB = 0;

        for (int i = 0; i < k; i++) {
            hashB = (hashB * base + b.charAt(i)) % mod;
        }

        for (int i = 0; i <= b.length() - k; i++) {
            ArrayList<Integer> list = windows.get(hashB);

            if (list == null) {
                list = new ArrayList<>();
                windows.put(hashB, list);
            }

            list.add(i);

            if (i < b.length() - k) {
                hashB = (hashB - b.charAt(i) * power % mod + mod) % mod;
                hashB = (hashB * base + b.charAt(i + k)) % mod;
            }
        }

        long hashA = 0;

        for (int i = 0; i < k; i++) {
            hashA = (hashA * base + a.charAt(i)) % mod;
        }

        int count = 0;

        for (int i = 0; i <= a.length() - k; i++) {
            ArrayList<Integer> candidates = windows.get(hashA);

            if (candidates != null) {
                for (int position : candidates) {
                    boolean same = true;

                    for (int j = 0; j < k; j++) {
                        if (a.charAt(i + j) != b.charAt(position + j)) {
                            same = false;
                            break;
                        }
                    }

                    if (same) {
                        count++;
                        break;
                    }
                }
            }

            if (i < a.length() - k) {
                hashA = (hashA - a.charAt(i) * power % mod + mod) % mod;
                hashA = (hashA * base + a.charAt(i + k)) % mod;
            }
        }

        return count;
    }
}
