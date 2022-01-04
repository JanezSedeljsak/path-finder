public class Test {
    public static void main(String[] args) throws Exception {
        Labyrinth lab = new Labyrinth();
        lab.loadDataFromFile("./labyrinths/labyrinth_8.txt");

        // v iskalnem algoritmu se rešitev shrani v Solution razred
        BFS.fullSearch(lab);

        // če hočeš da ti izriše graf
        lab.setDraw(true);

        // Solution.generateCSVForEveryLabyrinth();
    }
}
