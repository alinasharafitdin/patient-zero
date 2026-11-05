public class Outbreak {

    public static boolean[] cheapestTests(int n, int[][] nb, int[] cost) {
        boolean[] tested = new boolean[n];

        if (n == 0) {
            return tested;
        }

        long[][] dp = new long[n][2];

        calculate(0, -1, nb, cost, dp);

        boolean testRoot = dp[0][1] <= dp[0][0];
        restore(0, -1, testRoot, nb, dp, tested);

        return tested;
    }

    private static void calculate(int v, int parent, int[][] nb,
                                  int[] cost, long[][] dp) {
        dp[v][0] = 0;
        dp[v][1] = cost[v];

        for (int child : nb[v]) {
            if (child == parent) {
                continue;
            }

            calculate(child, v, nb, cost, dp);

            dp[v][0] += dp[child][1];
            dp[v][1] += Math.min(dp[child][0], dp[child][1]);
        }
    }

    private static void restore(int v, int parent, boolean take,
                                int[][] nb, long[][] dp,
                                boolean[] tested) {
        tested[v] = take;

        for (int child : nb[v]) {
            if (child == parent) {
                continue;
            }

            boolean takeChild;

            if (!take) {
                takeChild = true;
            } else {
                takeChild = dp[child][1] <= dp[child][0];
            }

            restore(child, v, takeChild, nb, dp, tested);
        }
    }

    public static int longestChain(int n, int[][] nb) {
        if (n == 0) {
            return 0;
        }

        int[] best = new int[1];
        depth(0, -1, nb, best);
        return best[0];
    }

    private static int depth(int v, int parent, int[][] nb, int[] best) {
        int first = 0;
        int second = 0;

        for (int child : nb[v]) {
            if (child == parent) {
                continue;
            }

            int length = depth(child, v, nb, best) + 1;

            if (length > first) {
                second = first;
                first = length;
            } else if (length > second) {
                second = length;
            }
        }

        best[0] = Math.max(best[0], first + second);

        return first;
    }
}