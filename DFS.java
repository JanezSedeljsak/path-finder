import java.util.*;

public class DFS {
    public static boolean[][] search(Labyrinth lab) {
        boolean[][] marked = new boolean[lab.h][lab.w];
        HashMap<Point, Point> from = new HashMap<>();

        Stack<Point> stack = new Stack<>();

        from.put(lab.start, null);
        marked[lab.start.y][lab.start.x] = true;
        stack.push(lab.start);

        System.out.println("Polagam na sklad vozlisce " + lab.start);

        while (!stack.isEmpty()) {
            Point curNode = stack.peek();

            if (lab.treasures.contains(curNode)) {
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
            for (Point move: Point.moveOptions) {
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
