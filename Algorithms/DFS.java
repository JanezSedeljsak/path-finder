package Algorithms;

import java.util.*;
import Core.*;

public class DFS {
    static Point dfsStartPoint;

    public static void fullSearch(Labyrinth lab) {
        Solution.reset(lab.h, lab.w);
		ArrayList<Point> goals = lab.treasures;
		dfsStartPoint = lab.start;

		while (goals.size() > 0) {
			search(lab, dfsStartPoint, goals);
		}

		goals.add(lab.end);
		search(lab, dfsStartPoint, goals);
	}

    public static void search(Labyrinth lab, Point start, ArrayList<Point> goals) {
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
                dfsStartPoint = curNode;
                goals.remove(curNode);
                
                LinkedList<Point> path = new LinkedList<>();
                while (true) {
                    path.addFirst(curNode);
                    curNode = from.get(curNode);
                    if (curNode != null) {
                        lab.drawCircleSTD(curNode.x, curNode.y);
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
                    stack.push(nextNode);
                    found = true;
                    break;
                }
            }

            if (!found) {
                stack.pop();
            }
        }

        return;
    }
}
