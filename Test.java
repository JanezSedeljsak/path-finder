public class Test {
    public static void main(String[] args) throws Exception {
        Labyrinth lab = new Labyrinth();
        lab.loadDataFromFile("./labyrinths/labyrinth_4.txt");

        // če hočeš animacijo
        //lab.setAnimated(true);

        // v iskalnem algoritmu se rešitev shrani v Solution razred
        //IDAStar.fullSearch(lab);

        // če hočeš da ti izriše graf
        //lab.setDraw(true);

        Solution.generateCSV();
    }
}
