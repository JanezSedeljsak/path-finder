package Algorithms;

import java.util.*;
import Core.*;
import Lib.Tuple;

public class AStar {
    static Point aStarStartPoint;
    static boolean bruteForce = false;

    public static void bruteForceSearch(Labyrinth lab) {
        LinkedList<Point> best = null;
        int bestPrice = Integer.MAX_VALUE;
        float avgCost = lab.calcAvgCost();
        int[][] hCost;

        Solution.reset(lab.h, lab.w);
        bruteForce = true;

        ArrayList<ArrayList<Point>> permutations = Labyrinth.permutations(lab.treasures);
        for (ArrayList<Point> permutation: permutations) {
            LinkedList<Point> tmpPath = new LinkedList<>();
            aStarStartPoint = lab.start;
            int tmpPrice = 0;

            while (permutation.size() > 0) {
                hCost = Labyrinth.hScoreGrid(lab, permutation.get(0), avgCost);
                Tuple<Integer, LinkedList<Point>> response = search(lab, aStarStartPoint, permutation, hCost);
    
                tmpPrice += response.first;
                for (Point point: response.second) {
                    if (tmpPath.size() == 0 || !point.equals(tmpPath.getLast())) {
                        tmpPath.add(point);
                    }
                }
            }
    
            permutation.add(lab.end);
            hCost = Labyrinth.hScoreGrid(lab, lab.end, avgCost);
            Tuple<Integer, LinkedList<Point>> response = search(lab, aStarStartPoint, permutation, hCost);
            tmpPrice += response.first;
            for (Point point: response.second) {
                if (tmpPath.size() == 0 || !point.equals(tmpPath.getLast())) {
                    tmpPath.add(point);
                }
            }
    
            if (tmpPrice < bestPrice) {
                bestPrice = tmpPrice;
                best = tmpPath;
            }
        }

        Solution.appendSolutionPath(best);
        for (Point p: best) {
            lab.drawCircleSTD(p.x, p.y, false);
        }
	}

    public static void fullSearch(Labyrinth lab, boolean isWeighted) {
        Solution.reset(lab.h, lab.w);
		ArrayList<Point> goals = lab.treasures;
        aStarStartPoint = lab.start;
        float avgCost = isWeighted ? lab.calcAvgCost() : 1f;
        int[][] hCost;

		while (goals.size() > 0) {
            int idx = Labyrinth.getNearestTreasure(goals, aStarStartPoint);
            hCost = Labyrinth.hScoreGrid(lab, goals.get(idx), avgCost);
			search(lab, aStarStartPoint, goals, hCost);
		}

		goals.add(lab.end);
        hCost = Labyrinth.hScoreGrid(lab, lab.end, avgCost);
		search(lab, aStarStartPoint, goals, hCost);
	}

    public static Tuple<Integer, LinkedList<Point>> search(Labyrinth lab, Point start, ArrayList<Point> goals, int[][] hCost) {
        LinkedList<Point> open = new LinkedList<>();
        boolean[][] closed = new boolean[lab.h][lab.w];
        HashMap<Point, Point> from = new HashMap<>();

        int[][] gScore = new int[lab.h][lab.w];
        int[][] fScore = new int[lab.h][lab.w];

        for (int i = 0; i < lab.h; i++) {
            for (int j = 0; j < lab.w; j++) {
                gScore[i][j] = Integer.MAX_VALUE;
                fScore[i][j] = Integer.MAX_VALUE;
            }
        }

        gScore[start.y][start.x] = 0;
        fScore[start.y][start.x] = hCost[start.y][start.x];
        Solution.visited[start.y][start.x] = true;
        from.put(start, null);
        open.add(start);

        while (!open.isEmpty()) {
            int minVal = Integer.MAX_VALUE;
            int minPos = 0;
            Point curNode = null;

            for (int i = 0; i < open.size(); i++) {
                Point node = open.get(i);
                if (fScore[node.y][node.x] < minVal) {
                    minVal = fScore[node.y][node.x];
                    minPos = i;
                    curNode = node;
                }
            }

            open.remove(minPos);
            closed[curNode.y][curNode.x] = true;
            lab.drawRectSTD(curNode.x, curNode.y);

            if (goals.contains(curNode)) {
                aStarStartPoint = curNode;
                goals.remove(curNode);

                LinkedList<Point> path = new LinkedList<>();
                int price = 0;

                while (true) {
                    path.addFirst(curNode);
                    price += lab.data[curNode.y][curNode.x] > 0 ? lab.data[curNode.y][curNode.x] : 0;

                    curNode = from.get(curNode);
                    if (curNode != null) {
                        if (!bruteForce) {
                            lab.drawCircleSTD(curNode.x, curNode.y, true);
                        }
                    } else {
                        break;                        
                    }
                }

                if (!bruteForce) {
                    Solution.appendSolutionPath(path);
                }
    
                return new Tuple<>(price, path);
            }

            for (Point move : Point.moveOptions) {
                Point nextNode = new Point(curNode.x + move.x, curNode.y + move.y);
                if (lab.isValidMove(nextNode.x, nextNode.y) && !closed[nextNode.y][nextNode.x]) {
                    open.add(nextNode);

                    int dist = gScore[curNode.y][curNode.x] + lab.data[nextNode.y][nextNode.x];
                    if (dist < gScore[nextNode.y][nextNode.x]) {
                        from.put(nextNode, curNode);
                        gScore[nextNode.y][nextNode.x] = dist;
                        fScore[nextNode.y][nextNode.x] = gScore[nextNode.y][nextNode.x] + hCost[nextNode.y][nextNode.x];
                    }
                }
            }
        }

        return null;
    }
}
