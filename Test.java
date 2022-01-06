import Algorithms.*;
import Core.*;

public class Test {
    public static void main(String[] args) throws Exception {
        Labyrinth lab = new Labyrinth();
        lab.loadDataFromFile("./labyrinths/labyrinth_6.txt");

        // če hočeš animacijo
        lab.setAnimated(true);

        // če želimo zapreti poti v prazno
        lab.blockDeadEnds();

        // v iskalnem algoritmu se rešitev shrani v Solution razred
        SimulatedAnnealingDFS.fullSearch(lab, 100);

        // če hočeš da ti izriše graf
        //lab.setDraw(true);

        // če hočeš izpis osnovne statisike in zaporedja obiskanih vozlišč
        lab.printStats("SimulatedAnnealingDFS");
    }
}
