import java.util.*;

public class DFS {
    static Point dfsStartPoint;

    public static boolean[][] fullSearch(Labyrinth lab) {
        boolean[][] marked = new boolean[lab.h][lab.w];
        ArrayList<Point> goals = lab.treasures;
        dfsStartPoint = lab.start;

        while (goals.size() > 0) {
            boolean[][] curMarked = search(lab, dfsStartPoint, goals);
            for (int i = 0; i < lab.h; i++) {
                for (int j = 0; j < lab.w; j++) {
                    marked[i][j] = marked[i][j] || curMarked[i][j];
                }
            }
        }

        goals.add(lab.end);
        boolean[][] curMarked = search(lab, dfsStartPoint, goals);
        for (int i = 0; i < lab.h; i++) {
            for (int j = 0; j < lab.w; j++) {
                marked[i][j] = marked[i][j] || curMarked[i][j];
            }
        }

        return marked;
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
                // remove treasure after visited
                dfsStartPoint = curNode;
                goals.remove(curNode);
                System.out.println("Resitev DFS v vozliscu " + curNode);
                System.out.print("Pot: " + curNode);

                while (true) {
                    curNode = from.get(curNode);
                    if (curNode != null)
                        System.out.print(" <-- " + curNode);
                    else
                        break;
                }

                return marked;
            }

            // najdi neobiskanega naslednjika
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
