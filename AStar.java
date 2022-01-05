import java.util.*;

public class AStar {
    static Point aStarStartPoint;

    public static void fullSearch(Labyrinth lab) {
        Solution.reset(lab.h, lab.w);
		ArrayList<Point> goals = lab.treasures;
        aStarStartPoint = lab.start;
        int[][] hCost;

		while (goals.size() > 0) {
            int idx = Labyrinth.getNearestTreasre(goals, aStarStartPoint);
            hCost = Labyrinth.hScoreGrid(lab, goals.get(idx));
			search(lab, aStarStartPoint, goals, hCost);
		}

		goals.add(lab.end);
        hCost = Labyrinth.hScoreGrid(lab, lab.end);
		search(lab, aStarStartPoint, goals, hCost);
	}

    public static void search(Labyrinth lab, Point start, ArrayList<Point> goals, int[][] hCost) {
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

                while (true) {
                    Solution.foundPath[curNode.y][curNode.x]++;
                    curNode = from.get(curNode);
                    if (curNode != null) {
                        lab.drawCircleSTD(curNode.x, curNode.y);
                    } else {
                        break;                        
                    }
                }

                return;
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
    }
}
