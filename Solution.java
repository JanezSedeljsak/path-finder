import java.util.*;

public class Solution {
    static boolean[][] visited;
    static int[][] foundPath;

    public static void reset(int h, int w) {
        visited = new boolean[h][w];
        foundPath = new int[h][w];
    }
}
