package Algorithms;

import java.util.*;
import Core.*;

public class BFS {
    static Point bfsStartPoint;

    public static void fullSearch(Labyrinth lab) {
        Solution.reset(lab.h, lab.w);
        ArrayList<Point> goals = lab.treasures;
        bfsStartPoint = lab.start;

        while (goals.size() > 0) {
            search(lab, bfsStartPoint, goals);
        }

        goals.add(lab.end);
        search(lab, bfsStartPoint, goals);
    }

    public static void search(Labyrinth lab, Point start, ArrayList<Point> goals) {
        boolean[][] marked = new boolean[lab.h][lab.w];
        HashMap<Point, Point> from = new HashMap<>();
        Queue<Point> queue = new LinkedList<>();

        from.put(start, null);
        Solution.visited[start.y][start.x] = true;
        marked[start.y][start.x] = true;
        queue.add(start);

        while (!queue.isEmpty()) {
            Point curNode = queue.peek();
            lab.drawRectSTD(curNode.x, curNode.y);

            if (goals.contains(curNode)) {
                bfsStartPoint = curNode;
                goals.remove(curNode);

                LinkedList<Point> path = new LinkedList<>();
                while (true) {
                    path.addFirst(curNode);
                    curNode = from.get(curNode);
                    if (curNode != null) {
                        lab.drawCircleSTD(curNode.x, curNode.y, true);
                    }
                    else
                        break;
                }

                Solution.appendSolutionPath(path);
                return;
            }

            boolean found = false;
            for (Point move : Point.moveOptions) {
                Point nextNode = new Point(curNode.x + move.x, curNode.y + move.y);
                if (lab.isValidMove(nextNode.x, nextNode.y) && !marked[nextNode.y][nextNode.x]) {
                    marked[nextNode.y][nextNode.x] = true;
                    from.put(nextNode, curNode);
                    queue.add(nextNode);
                    found = true;
                    break;
                }
            }

            if (!found) {
                queue.remove();
            }
        }

        return;
    }
}
