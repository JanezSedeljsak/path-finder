package Algorithms;

import java.util.*;
import Core.*;

public class GBFS {
    static Point gbfsStartPoint;

    public static void fullSearch(Labyrinth lab) {
        Solution.reset(lab.h, lab.w);
        ArrayList<Point> goals = lab.treasures;
        gbfsStartPoint = lab.start;
        int[][] hCost;

        while (goals.size() > 0) {
            int idx = Labyrinth.getNearestTreasre(goals, gbfsStartPoint);
            hCost = Labyrinth.hScoreGrid(lab, goals.get(idx), 1f);
            search(lab, gbfsStartPoint, goals, hCost);
        }

        goals.add(lab.end);
        hCost = Labyrinth.hScoreGrid(lab, lab.end, 1f);
        search(lab, gbfsStartPoint, goals, hCost);
    }

    public static void search(Labyrinth lab, Point start, ArrayList<Point> goals, int[][] hCost) {
        LinkedList<Point> open = new LinkedList<>();
        boolean[][] closed = new boolean[lab.h][lab.w];
        HashMap<Point, Point> from = new HashMap<>();

        Solution.visited[start.y][start.x] = true;
        from.put(start, null);

        open.add(start);

        while (!open.isEmpty()) {
            int minVal = Integer.MAX_VALUE;
            int minPos = 0;
            Point curNode = null;

            for (int i = 0; i < open.size(); i++) {
                Point node = open.get(i);
                if (hCost[node.y][node.x] < minVal) {
                    minVal = hCost[node.y][node.x];
                    minPos = i;
                    curNode = node;
                }
            }

            open.remove(minPos);
            closed[curNode.y][curNode.x] = true;
            lab.drawRectSTD(curNode.x, curNode.y);

            if (goals.contains(curNode)) {
                gbfsStartPoint = curNode;
                goals.remove(curNode);

                LinkedList<Point> path = new LinkedList<>();
                while (true) {
                    path.addFirst(curNode);
                    curNode = from.get(curNode);
                    if (curNode != null) {
                        lab.drawCircleSTD(curNode.x, curNode.y);
                    } else {
                        break;
                    }
                }
                
                Solution.appendSolutionPath(path);
                return;
            }

            for (Point move : Point.moveOptions) {
                Point nextNode = new Point(curNode.x + move.x, curNode.y + move.y);
                if (lab.isValidMove(nextNode.x, nextNode.y) && !closed[nextNode.y][nextNode.x]) {
                    open.add(nextNode);
                    from.put(nextNode, curNode);
                }
            }
        }
    }
}
