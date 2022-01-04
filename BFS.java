import java.util.*;

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

        System.out.println("Dajem v vrsto vozlisce " + start);

        while (!queue.isEmpty()) {
            Point curNode = queue.peek();

            if (goals.contains(curNode)) {
                bfsStartPoint = curNode;
                goals.remove(curNode);

                System.out.println("Resitev BFS v vozliscu " + curNode);
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
                    queue.add(nextNode);

                    System.out.println("Dajem v vrsto vozlisce " + nextNode);
                    found = true;
                    break;
                }
            }

            if (!found) {
                queue.remove();
                System.out.println("Odstranjujem iz vrste vozlisce " + curNode);
            }
        }

        return;
    }
}
