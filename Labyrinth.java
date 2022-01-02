import java.util.*;
import java.io.*;

class Point {
    int x, y;
    Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public String toString() {
        return String.format("[%s, %s]", x, y);
    }
}

public class Labyrinth {
    public Integer[][] data;
    public ArrayList<Point> treasures;
    public Point start, end;

    public static Labyrinth fromFile(String fileName) {
        Labyrinth lab = new Labyrinth();
        ArrayList<Integer[]> tmpData = new ArrayList<>();

        try {
            int rowNum = 0;
            String line;
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            while ((line = reader.readLine()) != null) {
                String[] splicedLine = line.split(",");
                Integer[] tmpRow = new Integer[splicedLine.length];
                for (int i = 0; i < splicedLine.length; i++) {
                    int num = Integer.parseInt(splicedLine[i]);
                    tmpRow[i] = num;

                    switch (num) {
                        case -2:
                            lab.setStart(rowNum, i);
                            break;
                        case -4:
                            lab.setEnd(rowNum, i);
                            break;
                        case -3:
                            lab.addTreasure(rowNum, i);
                            break;
                        default:
                            break;
                    }
                }
    
                tmpData.add(tmpRow);
                rowNum++;
            }
    
            reader.close();
        } catch (IOException ex) {
            System.out.printf("Error while reading: %s\n", ex.toString());
        }

        // if data is loaded convert from ArrayList<> -> Array
        int len = tmpData.size();
        if (len > 0) {
            int rowLen = tmpData.get(0).length;
            Integer[][] finalData = new Integer[len][rowLen];
            for (int i = 0; i < finalData.length; i++) {
                finalData[i] = tmpData.get(i);
            }

            lab.data = finalData;
        }

        return lab;
    }

    public Labyrinth() {
        this.treasures = new ArrayList<>();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Integer[] row: data) {
            String seperator = "";
            for (Integer cell: row) {
                sb.append(seperator + cell);
                seperator = " ";
            }
            sb.append("\n");
        }

        return sb.toString();
    }

    public void setStart(int x, int y) {
        this.start = new Point(x, y);
    }

    public void setEnd(int x, int y) {
        this.end = new Point(x, y);
    }


    public void addTreasure(int x, int y) {
        this.treasures.add(new Point(x, y));
    }
}
