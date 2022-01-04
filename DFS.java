import java.util.*;

public class DFS {
    static Point dfsStartPoint;

    public static boolean[][] addNewMarked(boolean[][] list1, boolean[][] list2) {
        for (int i = 0; i < list1.length; i++) {
            for (int j = 0, len = list1[0].length; j < len; j++) {
                list1[i][j] = list1[i][j] || list2[i][j];
            }
        }

        return list1;
    }

    public static void fullSearch(Labyrinth lab) {
        Solution.reset(lab.h, lab.w);
		boolean[][] marked = new boolean[lab.h][lab.w];
		ArrayList<Point> goals = lab.treasures;
		dfsStartPoint = lab.start;

		while (goals.size() > 0) {
			boolean[][] newMarked = search(lab, dfsStartPoint, goals);
			marked = addNewMarked(marked, newMarked);
		}

		goals.add(lab.end);
		boolean[][] newMarked = search(lab, dfsStartPoint, goals);
		marked = addNewMarked(marked, newMarked);

		Solution.visited = marked;
	}

    public static boolean[][] search(Labyrinth lab, Point start, ArrayList<Point> goals) {
        boolean[][] marked = new boolean[lab.h][lab.w];
        HashMap<Point, Point> from = new HashMap<>();
        Stack<Point> stack = new Stack<>();

        from.put(start, null);
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

                return marked;
            }

            boolean found = false;
            for (Point move : Point.moveOptions) {
                Point nextNode = new Point(curNode.x + move.x, curNode.y + move.y);
                if (lab.isValidMove(nextNode.x, nextNode.y) && !marked[nextNode.y][nextNode.x]) {
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

        return marked;
    }
}
