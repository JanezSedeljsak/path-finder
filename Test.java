public class Test {
    public static void main(String[] args) {
        Labyrinth lab = new Labyrinth();
        lab.loadDataFromFile("./labyrinths/labyrinth_7.txt");

        System.out.println(lab);
        System.out.println(lab.start);
        System.out.println(lab.end);

        boolean[][] foundPath = DFS.search(lab);
        Labyrinth.path = foundPath;
        // če hočeš da ti izriše graf
        lab.setDraw(true);
    }
}
