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

    public static void generateCSV() throws Exception {
        String[] algos = new String[] { "BFS", "DFS", "IDDFS", "AStar", "IDAStar" };

        for (String algo : algos) {
            String outFile = String.format("./results/%s.txt", algo);
            PrintWriter writer = new PrintWriter(outFile, "UTF-8");
            writer.write(csvHeader());

            System.out.printf("Started %s.................\n", algo);
            for (int i = 1; i <= 9; i++) {
                Labyrinth lab = new Labyrinth();
                lab.loadDataFromFile(String.format("./labyrinths/labyrinth_%d.txt", i));

                switch (algo) {
                    case "BFS":
                        BFS.fullSearch(lab);
                        writer.write(csvRow(lab, "BFS"));
                        break;
                    case "DFS":
                        DFS.fullSearch(lab);
                        writer.write(csvRow(lab, "DFS"));
                        break;
                    case "IDDFS":
                        IDDFS.fullSearch(lab);
                        writer.write(csvRow(lab, "IDDFS"));
                        break;
                    case "AStar":
                        AStar.fullSearch(lab);
                        writer.write(csvRow(lab, "AStar"));
                        break;
                    case "IDAStar":
                        IDAStar.fullSearch(lab);
                        writer.write(csvRow(lab, "IDAStar"));
                        break;
                }
            }
            writer.close();
        }
    }
}
