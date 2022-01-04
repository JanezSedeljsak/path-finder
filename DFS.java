import java.util.*;

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

        System.out.println("Polagam na sklad vozlisce " + start);

        while (!stack.isEmpty()) {
            Point curNode = stack.peek();

            if (goals.contains(curNode)) {
                dfsStartPoint = curNode;
                goals.remove(curNode);

                System.out.println("Resitev DFS v vozliscu " + curNode);
                System.out.print("Pot: " + curNode);

                while (true) {
                    Solution.foundPath[curNode.y][curNode.x]++;
                    curNode = from.get(curNode);
                    if (curNode != null)
                        System.out.print(" <-- " + curNode);
                    else
                        break;
                }

                return;
            }

            boolean found = false;
            for (Point move : Point.moveOptions) {
                Point nextNode = new Point(curNode.x + move.x, curNode.y + move.y);
                if (lab.isValidMove(nextNode.x, nextNode.y) && !marked[nextNode.y][nextNode.x]) {
                    Solution.visited[nextNode.y][nextNode.x] = true;
                    marked[nextNode.y][nextNode.x] = true;
                    from.put(nextNode, curNode);
                    stack.push(nextNode);

                    System.out.println("Polagam na sklad vozlisce " + nextNode);
                    found = true;
                    break;
                }
            }

            if (!found) {
                stack.pop();
                System.out.println("Odstranjum s sklada vozlisce " + curNode);
            }
        }

        return;
    }
}
