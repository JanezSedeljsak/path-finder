package Core;

import java.io.*;
import java.util.*;
import Algorithms.*;

public class Solution {
    public static boolean[][] visited;
    public static int[][] foundPath;
    public static LinkedList<Point> directedPath;

    public static void reset(int h, int w) {
        visited = new boolean[h][w];
        foundPath = new int[h][w];
        directedPath = new LinkedList<>();
    }

    public static void appendSolutionPath(LinkedList<Point> path) {
        for (Point point: path) {
            foundPath[point.y][point.x]++;
            if (directedPath.size() == 0 || !point.equals(directedPath.getLast())) {
                directedPath.addLast(point);
            }
        }
    }

    public static String csvHeader() {
        return "algorithm, visitedCount, hallwayLength, pathLength, pathPrice, executionTime\n";
    }

    public static int[] calcStats(Labyrinth lab) {
        int visitedCount = 0, pathLength = 0, pathPrice = 0, hallwayLength = 0;

        for (int i = 0; i < lab.h; i++) {
            for (int j = 0, len = lab.w; j < len; j++) {
                pathLength += foundPath[i][j];
                if (lab.data[i][j] > 0) {
                    pathPrice += foundPath[i][j] * lab.data[i][j];
                    visitedCount += visited[i][j] ? 1 : 0;
                    hallwayLength++;
                }
            }
        }

        return new int[] { visitedCount, hallwayLength, pathLength, pathPrice };
    }

    public static String csvRow(Labyrinth lab, String algorithm, long executionTime) {
        int stats[] = calcStats(lab);

        return String.format(Locale.ENGLISH, "%s, %d, %d, %d, %d, %d\n",
                algorithm, stats[0], stats[1], stats[2], stats[3], executionTime);
    }

    public static String printStats(Labyrinth lab, String algorithm) {
        int stats[] = calcStats(lab);

        String strHeadStruct = "%25s %15s %15s %15s %15s\n";
        String strRowStruct = "%25s %15d %15d %15d %15d\n";

        StringBuilder sb = new StringBuilder();
        String seperator = "-----------------------------------------------------------------------------------------\n";
        sb.append(seperator);
        sb.append(String.format(Locale.ENGLISH, strHeadStruct, "Algorithm", "Visited count", "Hallway length", "Path length", "Path price"));
        sb.append(seperator);
        sb.append(String.format(Locale.ENGLISH, strRowStruct, algorithm, stats[0], stats[1], stats[2], stats[3]));
        sb.append(seperator);
        sb.append("Path: ");

        String arrow = "";
        for (Point point: directedPath) {
            sb.append(arrow + point.toString());
            arrow = " -> ";
        }

        return sb.toString();
    }

    public static void generateCSV() throws Exception {
        String[] algos = new String[] { "BFS", "DFS", "IDDFS", "GBFS", "AStar", "AStarWeighted", "IDAStar" };

        for (String algo : algos) {
            String outFile = String.format("./results/%s.txt", algo);
            PrintWriter writer = new PrintWriter(outFile, "UTF-8");
            writer.write(csvHeader());

            System.out.printf("Started %s.................\n", algo);
            for (int i = 1; i <= 9; i++) {
                Labyrinth lab = new Labyrinth();
                lab.loadDataFromFile(String.format("./labyrinths/labyrinth_%d.txt", i));

                long startTime = System.nanoTime();
                switch (algo) {
                    case "BFS":
                        BFS.fullSearch(lab);
                        writer.write(csvRow(lab, "BFS", System.nanoTime() - startTime));
                        break;
                    case "DFS":
                        DFS.fullSearch(lab);
                        writer.write(csvRow(lab, "DFS", System.nanoTime() - startTime));
                        break;
                    case "IDDFS":
                        IDDFS.fullSearch(lab);
                        writer.write(csvRow(lab, "IDDFS", System.nanoTime() - startTime));
                        break;
                    case "GBFS":
                        GBFS.fullSearch(lab);
                        writer.write(csvRow(lab, "GBFS", System.nanoTime() - startTime));
                        break;
                    case "AStar":
                        AStar.fullSearch(lab, false);
                        writer.write(csvRow(lab, "AStar", System.nanoTime() - startTime));
                        break;
                    case "AStarWeighted":
                        AStar.fullSearch(lab, true);
                        writer.write(csvRow(lab, "AStarWeighted", System.nanoTime() - startTime));
                        break;
                    case "IDAStar":
                        IDAStar.fullSearch(lab);
                        writer.write(csvRow(lab, "IDAStar", System.nanoTime() - startTime));
                        break;
                }
            }
            writer.close();
        }
    }

    public static void main(String[] args) throws Exception {
        generateCSV();
    }
}
