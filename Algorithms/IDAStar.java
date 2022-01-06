package Algorithms;

import java.util.*;
import Core.*;

public class IDAStar {
    static Point iDAStarPoint;
    Labyrinth searchLab;
    ArrayList<Point> searchEndNodes;
    int[][] searchHeurCost;

    LinkedList<Point> path;
    boolean found;

    private int search(int gScore, int bound) {
        Point curNode = path.get(0);
        searchLab.drawRectSTD(curNode.x, curNode.y);

        int fScore = gScore + searchHeurCost[curNode.y][curNode.x];
        if (fScore > bound) {
            return fScore;
        }

        if (searchEndNodes.contains(curNode)) {
            iDAStarPoint = curNode;
            searchEndNodes.remove(curNode);
            found = true;
            return fScore;
        }

        int min = Integer.MAX_VALUE;
        for (Point move : Point.moveOptions) {
            Point nextNode = new Point(curNode.x + move.x, curNode.y + move.y);
            if (searchLab.isValidMove(nextNode.x, nextNode.y) && !path.contains(nextNode)) {
                path.add(0, nextNode);
                int res = search(gScore + searchLab.data[nextNode.y][nextNode.x], bound);
                if (found) {
                    return res;
                }
                    
                if (res < min) {
                    min = res;
                }

                path.remove(0);
            }
        }

        return min;
    }

    public void find(Labyrinth lab, Point start, ArrayList<Point> goals, int[][] hCost) {
        searchLab = lab;
        searchEndNodes = goals;
        searchHeurCost = hCost;

        path = new LinkedList<>();
        path.add(start);
        Solution.visited[start.y][start.x] = true;
        int bound = searchHeurCost[start.y][start.x];
        found = false;

        while (true) {
            int res = search(0, bound);
            if (found) {
                for (int i = 0; i < path.size(); i++) {
                    Point curNode = path.get(i);
                    Solution.foundPath[curNode.y][curNode.x]++;
                    searchLab.drawCircleSTD(curNode.x, curNode.y);
                }
                
                break;
            }

            if (res == Integer.MAX_VALUE) {
                break;
            }

            bound = res;
        }
    }

    public static void fullSearch(Labyrinth lab) {
        IDAStar idas = new IDAStar();
        Solution.reset(lab.h, lab.w);
        ArrayList<Point> goals = lab.treasures;
        iDAStarPoint = lab.start;
        int[][] hCost;

        while (goals.size() > 0) {
            int idx = Labyrinth.getNearestTreasre(goals, iDAStarPoint);
            hCost = Labyrinth.hScoreGrid(lab, goals.get(idx), 1f);
            idas.find(lab, iDAStarPoint, goals, hCost);
        }

        goals.add(lab.end);
        hCost = Labyrinth.hScoreGrid(lab, lab.end, 1f);
        idas.find(lab, iDAStarPoint, goals, hCost);
    }
}