import java.awt.*;
import java.io.*;
import javax.swing.*;
import java.util.*;

public class Labyrinth extends JFrame {
    public int[][] data;
    public int h, w;
    public ArrayList<Point> treasures;
    public Point start, end;
    public boolean isDraw = false;

    public void loadDataFromFile(String fileName) {
        ArrayList<int[]> tmpData = new ArrayList<>();

        try {
            int rowNum = 0;
            String line;
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            while ((line = reader.readLine()) != null) {
                String[] splicedLine = line.split(",");
                int[] tmpRow = new int[splicedLine.length];
                for (int i = 0; i < splicedLine.length; i++) {
                    int num = Integer.parseInt(splicedLine[i]);
                    tmpRow[i] = num;

                    switch (num) {
                        case -2:
                            setStart(i, rowNum);
                            break;
                        case -4:
                            setEnd(i, rowNum);
                            break;
                        case -3:
                            addTreasure(i, rowNum);
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
            int[][] finalData = new int[len][rowLen];
            for (int i = 0; i < finalData.length; i++) {
                finalData[i] = tmpData.get(i);
            }

            setData(finalData);
        }
    }

    public Labyrinth() {
        this.treasures = new ArrayList<>();
        this.h = 0;
        this.w = 0;
    }

    public void paint(Graphics g) {
        if (!isDraw || w == 0 || h == 0) {
            return;
        }

        g.translate(getInsets().left, getInsets().top);
        int topMargin = getInsets().top;
        int rightMargin = getInsets().right;
        int width = getWidth() - rightMargin;
        int height = getHeight() - topMargin;

        // draw background
        g.setColor(Color.GRAY);
        g.fillRect(0, 0, getWidth(), getHeight());

        int rowCount = h;
        int colCount = w;

        int cellWidth = width / colCount;
        int cellHeight = height / rowCount;

        for (int row = 0; row < h; row++) {
            for (int column = 0; column < w; column++) {
                int cellX = cellWidth * column;
                int cellY = cellHeight * row;

                int cellType = data[row][column];
                g.setColor(cellType == -1 ? Color.GRAY : ((Solution.visited != null && Solution.visited[row][column]) ? Color.PINK : Color.WHITE));
                g.fillRect(cellX, cellY, cellWidth, cellHeight);

                if (Solution.foundPath[row][column] > 0) {
                    g.setColor(Color.RED);
                    g.fillOval(cellX, cellY, cellWidth, cellHeight);
                }

                if (cellType < -1) {
                    switch (cellType) {
                        case -2: g.setColor(Color.BLUE); break;
                        case -3: g.setColor(Color.ORANGE); break;
                        case -4: g.setColor(Color.GREEN); break;
                    }

                    g.fillOval(cellX, cellY, cellWidth, cellHeight);
                }
            }
        }
    }

    public void setDraw(boolean isDraw) {
        boolean prev = this.isDraw;
        this.isDraw = isDraw;
        if (!prev && isDraw) {
            setSize(720, 640);
            setVisible(true);
            setTitle("Gray - wall, White - hallway, Red - path, Blue - start, Green - finish, Orange - reward");
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            getContentPane().setBackground(Color.WHITE);
        }
        this.repaint();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int[] row : data) {
            String seperator = "";
            for (int cell : row) {
                sb.append(seperator + cell);
                seperator = " ";
            }
            sb.append("\n");
        }

        return sb.toString();
    }

    public boolean isValidMove(int x, int y) {
        if (x < 0 || x >= w) return false;
        if (y < 0 || y >= h) return false;
        return data[y][x] != -1;
    }

    public void setData(int[][] data) {
        this.data = data;
        if (data.length > 0) {
            this.h = data.length;
            this.w = data[0].length;
        }
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
