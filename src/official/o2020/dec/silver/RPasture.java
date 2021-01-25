package official.o2020.dec.silver;

import java.io.*;
import java.util.*;

/**
 * 2020 dec silver
 * 4
 * 0 2
 * 1 0
 * 2 3
 * 3 5 should output 13
 */
public final class RPasture {
    public static void main(String[] args) throws IOException {
        BufferedReader read = new BufferedReader(new InputStreamReader(System.in));
        int cowNum = Integer.parseInt(read.readLine());

        HashSet<Integer> seenX = new HashSet<>();
        HashSet<Integer> seenY = new HashSet<>();
        int[][] cows = new int[cowNum][2];
        for (int c = 0; c < cowNum; c++) {
            cows[c] = Arrays.stream(read.readLine().split(" ")).mapToInt(Integer::parseInt).toArray();
            if (seenX.contains(cows[c][0]) || seenY.contains(cows[c][1])) {
                throw new IllegalArgumentException("look my man all the coordinates gotta be distinct");
            }
            seenX.add(cows[c][0]);
            seenY.add(cows[c][1]);
        }

        long start = System.currentTimeMillis();
        // because all the x's and y's are distinct, we can just sort of "compress" them into just 1, 2, 3, etc...
        Arrays.sort(cows, Comparator.comparingInt(c -> c[1]));
        HashMap<Integer, Integer> reducedY = new HashMap<>();
        for (int c = 0; c < cowNum; c++) {
            reducedY.put(cows[c][1], c);
        }
        Arrays.sort(cows, Comparator.comparingInt(c -> c[0]));
        HashMap<Integer, Integer> reducedX = new HashMap<>();
        for (int c = 0; c < cowNum; c++) {
            reducedX.put(cows[c][0], c);
        }
        for (int c = 0; c < cowNum; c++) {
            cows[c][0] = reducedX.get(cows[c][0]);
            cows[c][1] = reducedY.get(cows[c][1]);
        }

        // make prefix sums for how many points have a less than & greater than y value
        int[][] lessThanY = new int[cowNum][cowNum + 1];
        int[][] greaterThanY = new int[cowNum][cowNum + 1];
        for (int c = 0; c < cowNum; c++) {
            int thisY = cows[c][1];
            lessThanY[thisY] = new int[cowNum + 1];
            greaterThanY[thisY] = new int[cowNum + 1];
            for (int x = 1; x <= cowNum; x++) {
                lessThanY[thisY][x] = lessThanY[thisY][x - 1] + (cows[x - 1][1] < thisY ? 1 : 0);
                greaterThanY[thisY][x] = greaterThanY[thisY][x - 1] + (cows[x - 1][1] > thisY ? 1 : 0);
            }
        }

        long total = 0;
        for (int c1 = 0; c1 < cowNum; c1++) {
            for (int c2 = c1 + 1; c2 < cowNum; c2++) {
                int bottom = Math.min(cows[c1][1], cows[c2][1]);
                int top = Math.max(cows[c1][1], cows[c2][1]);
                int bottomTotal = 1 + lessThanY[bottom][c2 + 1] - lessThanY[bottom][c1];
                int topTotal = 1 + greaterThanY[top][c2 + 1] - greaterThanY[top][c1];
                total += (long) bottomTotal * topTotal;
            }
        }
        total += cowNum + 1;  // we didn't count the ones where fj just boxes either a single cow or no cow at all
        System.out.println(total);
        System.err.printf("i love when the cows are just treated as static objects: %d ms%n", System.currentTimeMillis() - start);
    }
}
