public class Test {
    public static void main(String[] args) {
        Labyrinth lab = new Labyrinth();
        lab.loadDataFromFile("./labyrinths/labyrinth_7.txt");

        // v iskalnem algoritmu se rešitev shrani v Solution razred
        IDDFS.fullSearch(lab);

        // če hočeš da ti izriše graf
        lab.setDraw(true);
    }
}
