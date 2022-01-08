package Core;

import java.awt.*;
import java.io.*;
import javax.swing.*;
import java.util.*;
import Lib.StdDraw;

public class Labyrinth extends JFrame {
    public int[][] data;
    public int h, w;
    public ArrayList<Point> treasures;
    public Point start, end;
    public boolean isDraw = false;
    public boolean isAnimated = false;
    private int animationDelay = 10;

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

    public static int manhattanDistance(Point a, Point b) {
        return Math.abs(a.x - b.x) + Math.abs(a.y - b.y);
    }

    public static int getNearestTreasure(ArrayList<Point> treasures, Point start) {
        int distance = Integer.MAX_VALUE;
        int indexOfBest = -1;

        for (int i = 0, len = treasures.size(); i < len; i++) {
            int tmpDistance = manhattanDistance(start, treasures.get(i));
            if (tmpDistance < distance) {
                distance = tmpDistance;
                indexOfBest = i;
            }
        }
      
        return indexOfBest;
    }

    public void blockDeadEnds() {
        for (int i = 0; i < h; i++) {
            for (int j = 0, len = w; j < len; j++) {
                if (data[i][j] > 0) {
                    recursiveDeadEndCheck(j, i);
                }
            }
        }
    }

    public void recursiveDeadEndCheck(int x, int y) {
        if (data[y][x] < 0) return;
        ArrayList<Point> openSpots = new ArrayList<>();
        for (Point move: Point.moveOptions) {
            Point nextNode = new Point(x + move.x, y + move.y);
            if (isValidMove(nextNode.x, nextNode.y)) {
                openSpots.add(nextNode);
            }
        }

        if (openSpots.size() > 1) {
            return;
        }

        data[y][x] = -1;
        if (isAnimated) {
            StdDraw.setPenColor(Color.DARK_GRAY);
            StdDraw.filledRectangle(x, y, 0.5, 0.5);
            StdDraw.show();
        }
        for (Point nextNode: openSpots) {
            recursiveDeadEndCheck(nextNode.x, nextNode.y);
        }
    }

    public float calcAvgCost() {
        int sum = 0, count = 0;
        for (int i = 0; i < h; i++) {
            for (int j = 0, len = w; j < len; j++) {
                if (data[i][j] > 0) {
                    sum +=  data[i][j];
                    count++;
                }
            }
        }
        
        return sum/(float)count;
    }

    public static int[][] hScoreGrid(Labyrinth lab, Point goal, float avgCost) {
        int [][]hScore = new int[lab.h][lab.w];

        for (int i = 0; i < hScore.length; i++) {
            for (int j = 0, len = hScore[0].length; j < len; j++) {
                float weightedScore = manhattanDistance(new Point(j, i), goal) * avgCost;
                hScore[i][j] = (int)weightedScore;
            }
        }

        return hScore;
    }

    public void drawBackgroundSTD() {
        if (isAnimated) {
            StdDraw.enableDoubleBuffering();
            StdDraw.setCanvasSize(720, 640);
            StdDraw.clear(Color.GRAY);
            StdDraw.setXscale(0, this.w - 1);
            StdDraw.setYscale(this.h - 1, 0);

            for (int x = 0; x < w; x++) {
                for (int y = 0; y < h; y++) {
                    int cellType = data[y][x];
                    if (cellType != -1) {
                        StdDraw.setPenColor(Color.WHITE);
                        StdDraw.filledRectangle(x, y, 0.5, 0.5);

                        switch (cellType) {
                            case -2:
                                StdDraw.setPenColor(Color.BLUE);
                                StdDraw.filledCircle(x, y, 0.5);
                                break;
                            case -3:
                                StdDraw.setPenColor(Color.ORANGE);
                                StdDraw.filledCircle(x, y, 0.5);
                                break;
                            case -4:
                                StdDraw.setPenColor(Color.GREEN);
                                StdDraw.filledCircle(x, y, 0.5);
                                break;
                        }
                    }
                }
            }
            StdDraw.show();
        }
    }

    public void drawRectSTD(int x, int y) {
        boolean wasAlreadyVisited = Solution.visited[y][x];
        Solution.visited[y][x] = true;

        if (isAnimated && (!wasAlreadyVisited || new Point(x,y).equals(start))) {
            if (Solution.foundPath[y][x] == 0) {
                StdDraw.setPenColor(Color.PINK);
                StdDraw.filledRectangle(x, y, 0.5, 0.5);

                switch (data[y][x]) {
                    case -2:
                        StdDraw.setPenColor(Color.BLUE);
                        StdDraw.filledCircle(x, y, 0.5);
                        break;
                    case -3:
                        StdDraw.setPenColor(Color.ORANGE);
                        StdDraw.filledCircle(x, y, 0.5);
                        break;
                    case -4:
                        StdDraw.setPenColor(Color.GREEN);
                        StdDraw.filledCircle(x, y, 0.5);
                        break;
                }
                StdDraw.show();
                StdDraw.pause(animationDelay);
            }
        }
    }

    public void drawCircleSTD(int x, int y) {
        if (isAnimated) {
            if (this.data[y][x] > 0) {
                StdDraw.setPenColor(Color.RED);
                StdDraw.filledCircle(x, y, 0.5);
                StdDraw.setPenColor(Color.BLACK);
                StdDraw.text(x, y, (Solution.foundPath[y][x] + 1) + "");
                StdDraw.show();
                StdDraw.pause(animationDelay);
            }
        }
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
                    g.setColor(Color.BLACK);
                    g.drawString(""+Solution.foundPath[row][column], cellX + (int)(cellWidth*.35), cellY + (int)(cellHeight*.65));
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

    public void setAnimated(boolean isAnimated) {
        this.isAnimated = isAnimated;
        drawBackgroundSTD();
    }

    public void printStats(String algorithm) {
        String out = Solution.printStats(this, algorithm);
        System.out.println(out);
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
