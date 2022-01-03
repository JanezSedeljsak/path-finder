public class Test {
    public static void main(String[] args) {
        Labyrinth lab = Labyrinth.fromFile("./labyrinths/labyrinth_1.txt");
        System.out.println(lab);
        System.out.println(lab.start);
        System.out.println(lab.end);

        Grid grid = new Grid(lab);
    }
}
