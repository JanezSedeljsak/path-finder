import java.util.*;
import java.io.*;

public class Solution {
    static boolean[][] visited;
    static int[][] foundPath;

    public static void reset(int h, int w) {
        visited = new boolean[h][w];
        foundPath = new int[h][w];
    }

    public static String csvHeader() {
        return "algorithm, visitedCount, pathLength, pathPrice\n";
    }

    public static String csvRow(Labyrinth lab, String algorithm) {
        int visitedCount = 0, pathLength = 0, pathPrice = 0;
        for (int i = 0; i < foundPath.length; i++) {
            for (int j = 0, len = foundPath[0].length; j < len; j++) {
                visitedCount += visited[i][j] ? 1 : 0;
                pathLength += foundPath[i][j];
                if (lab.data[i][j] > 0) {
                    pathPrice += foundPath[i][j] * lab.data[i][j];
                }
            }
        }

        return String.format("%s, %d, %d, %d\n", algorithm, visitedCount, pathLength, pathPrice);
    }

    public static void generateCSV(int labryinthNum) throws Exception {
        String inFile = String.format("./labyrinths/labyrinth_%d.txt", labryinthNum);
        String outFile = String.format("./results/labyrinth_%d.txt", labryinthNum);
        PrintWriter writer = new PrintWriter(outFile, "UTF-8");
        writer.write(csvHeader());
    
        Labyrinth lab = new Labyrinth();
        lab.loadDataFromFile(inFile);

        BFS.fullSearch(lab);
        writer.write(csvRow(lab, "BFS"));

        DFS.fullSearch(lab);
        writer.write(csvRow(lab, "DFS"));

        IDDFS.fullSearch(lab);
        writer.write(csvRow(lab, "IDDFS"));

        writer.close();
    }

    public static void generateCSVForEveryLabyrinth() throws Exception {
        for (int i = 1; i <= 9; i++) {
            generateCSV(i);
        }
    }

}
