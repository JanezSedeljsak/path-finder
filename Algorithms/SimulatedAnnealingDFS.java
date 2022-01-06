package Algorithms;

import java.util.*;
import Lib.Tuple;
import Core.*;

public class SimulatedAnnealingDFS {
    static Point dfsStartPoint;

    public static void fullSearch(Labyrinth lab) {
        Solution.reset(lab.h, lab.w);
        ArrayList<Point> goals = lab.treasures;
        dfsStartPoint = lab.start;
        while (goals.size() > 0) {
            simulatedAnnealing(lab, dfsStartPoint, goals);
        }

        goals.add(lab.end);
        simulatedAnnealing(lab, dfsStartPoint, goals);
    }

    public static void simulatedAnnealing(Labyrinth lab, Point start, ArrayList<Point> goals) {
        Tuple<Integer, LinkedList<Point>> best = null;
        int bestPrice = Integer.MAX_VALUE;
        int labManhattan = lab.h + lab.w;
        double t0 = labManhattan;

        while (--t0 > 0) {
            for (int i = 0; i < labManhattan / 5; i++) {
                Tuple<Integer, LinkedList<Point>> curResponse = search(lab, start, goals);
                if (curResponse.first < bestPrice) {
                    bestPrice = curResponse.first;
                    best = curResponse;
                } else {
                    double rand = Math.random() + 1;
                    int diff = bestPrice - curResponse.first;
                    if (rand > 1.2) {
                        continue;
                    }
                    
                    if (rand > Math.exp(-diff / t0)) {
                        bestPrice = curResponse.first;
                        best = curResponse;
                    }
                }
            }
        }
        

        if (best == null) {
            System.out.println("Didn't find path");
            return;
        }
        

        Point goal = best.second.getLast();
        dfsStartPoint = goal;
        goals.remove(goal);
        Solution.appendSolutionPath(best.second);
        for (Point p: best.second) {
            lab.drawCircleSTD(p.x, p.y);
        }
        
    }

    public static Tuple<Integer, LinkedList<Point>> search(Labyrinth lab, Point start, ArrayList<Point> goals) {
        boolean[][] marked = new boolean[lab.h][lab.w];
        HashMap<Point, Point> from = new HashMap<>();
        Stack<Point> stack = new Stack<>();

        from.put(start, null);
        Solution.visited[start.y][start.x] = true;
        marked[start.y][start.x] = true;
        stack.push(start);

        while (!stack.isEmpty()) {
            Point curNode = stack.peek();
            lab.drawRectSTD(curNode.x, curNode.y);

            if (goals.contains(curNode)) {
                int price = 0;
                LinkedList<Point> path = new LinkedList<>();
                while (true) {
                    path.addFirst(curNode);
                    price += lab.data[curNode.y][curNode.x] > 0 ? lab.data[curNode.y][curNode.x] : 0;
                    curNode = from.get(curNode);
                    if (curNode == null) {
                        break;
                    }
                }

                return new Tuple<>(price, path);
            }

            boolean found = false;
            for (Point move : Point.shuffledOptions()) {
                Point nextNode = new Point(curNode.x + move.x, curNode.y + move.y);
                if (lab.isValidMove(nextNode.x, nextNode.y) && !marked[nextNode.y][nextNode.x]) {
                    marked[nextNode.y][nextNode.x] = true;
                    from.put(nextNode, curNode);
                    stack.push(nextNode);
                    found = true;
                    break;
                }
            }

            if (!found) {
                stack.pop();
            }
        }

        return null;
    }
}