package official.o2018.jan.gold.itsAllPaintings;

import java.io.*;
import java.util.*;

/**
 * 2018 jan gold
 * (tbh the sol explains it better)
 * but basically all a painting needs to be valid is a consecutive length of colors
 * that's at least the length of a single stamp - then everything's free reign
 * we then use complementary counting and math voodoo to figure the amount of INVALID
 * paintings, then subtract that from the total amt
 */
public final class SPainting {
    private static final int MOD = (int) Math.pow(10, 9) + 7;
    public static void main(String[] args) throws IOException {
        long start = System.currentTimeMillis();
        StringTokenizer info = new StringTokenizer(new BufferedReader(new FileReader("spainting.in")).readLine());
        int canvasLen = Integer.parseInt(info.nextToken());
        int typeNum = Integer.parseInt(info.nextToken());
        int stampLen = Integer.parseInt(info.nextToken());

        long[] totalInvalid = new long[canvasLen + 1];
        long base = 1;
        for (int i = 1; i < stampLen; i++) {
            base = (base * typeNum) % MOD;
            totalInvalid[i] = (base + totalInvalid[i - 1]) % MOD;
        }

        for (int i = stampLen; i <= canvasLen; i++) {
            totalInvalid[i] = ((typeNum * totalInvalid[i - 1]) % MOD
                    - ((typeNum - 1) * totalInvalid[i - stampLen]) % MOD) % MOD;
        }

        long valid = 1;
        for (int i = 0; i < canvasLen; i++) {
            valid = (valid * typeNum) % MOD;
        }
        valid -= totalInvalid[canvasLen] - totalInvalid[canvasLen - 1];
        valid = (valid + MOD) % MOD;  // make the result positive (stupid modulus function)

        PrintWriter written = new PrintWriter("spainting.out");
        written.println(valid);
        written.close();
        System.out.println(valid);
        System.out.printf("you should watch fire force it's pretty good: %d ms%n", System.currentTimeMillis() - start);
    }
}