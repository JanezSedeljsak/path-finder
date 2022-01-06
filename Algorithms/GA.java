package Algorithms;

import Core.Labyrinth;
import Core.Point;
import Core.Solution;

import java.util.ArrayList;
import java.util.Random;

public class GA {
    static int fitness(Point[] individual, Labyrinth lab, int[][] hCost) {
        Point curNode = lab.start;
        boolean[][] marked = new boolean[lab.h][lab.w];

        for (Point point : individual) {
            ArrayList<Point> movesAvailable = new ArrayList<>();
            for (Point moveOption : Point.moveOptions) {
                if (lab.isValidMove(curNode.x + moveOption.x, curNode.y + moveOption.y) && !marked[curNode.y][curNode.x])
                    movesAvailable.add(moveOption);
            }
            if (movesAvailable.size() > 0) {
                lab.drawRectSTD(curNode.x, curNode.y);
                curNode = new Point(curNode.x + point.x, curNode.y + point.y);
            }
        }

        return hCost[curNode.x][curNode.y];
    }

    static int fittestScoreIndex(Point[][] population, int[][] hCost, int moveLimit, Labyrinth lab) {
        int fittestIndex = 0;

        for (int i = 1; i < population.length; i++) {
            if (fitness(population[i], lab, hCost) > fitness(population[fittestIndex], lab, hCost))
                fittestIndex = i;
        }
        return fittestIndex;
    }

    static void mutate(Point[] chromosome, double mutationChance) {
        Random r = new Random();
        if (mutationChance > Math.random()) {
            chromosome[r.nextInt(chromosome.length)] = Point.moveOptions[r.nextInt(4)];
        }
    }

    static Point[][] crossOver(Point[][] population, int moveLimit, double mutationChance, int[][] hCost, Labyrinth lab) {
        Random r = new Random();
        ArrayList<Point[]> bestPopulation = new ArrayList<>();
        ArrayList<Point[]> newPopulation = new ArrayList<>();

        for (int i = 0; i < population.length / 2; i++) {
            bestPopulation.add(population[fittestScoreIndex(population, hCost, moveLimit - 1, lab)]);
        }

        for (int i = 0; i < bestPopulation.size(); i++) {
            Point[] firstParent = bestPopulation.get(r.nextInt(bestPopulation.size()));
            Point[] secondParent = bestPopulation.get(r.nextInt(bestPopulation.size()));
            int crossOverPoint = r.nextInt(moveLimit - 1);

            Point[] child = new Point[moveLimit];

            for (int j = 0; j < moveLimit; j++) {
                if (j < crossOverPoint)
                    child[j] = firstParent[j];
                else
                    child[j] = secondParent[j];
            }

            mutate(child, mutationChance);
            newPopulation.add(child);
        }
        return newPopulation.toArray(new Point[0][0]);
    }

    public static void fullSearch(Labyrinth lab) {
        int moveLimit = 20;
        int populationSize = 2000;
        double mutationChance = 0.5;
        Random r = new Random();

        Solution.reset(lab.h, lab.w);

        int[][] hCost = Labyrinth.hScoreGrid(lab, lab.end, 1f);
        Point[][] population = new Point[populationSize][moveLimit];

        for (Point[] individual : population) {
            Point curNode = lab.start;
            boolean[][] marked = new boolean[lab.h][lab.w];
            for (int i = 0; i < individual.length; i++) {
                ArrayList<Point> movesAvailable = new ArrayList<>();
                for (Point moveOption : Point.moveOptions) {
                    if (lab.isValidMove(curNode.x + moveOption.x, curNode.y + moveOption.y) && !marked[curNode.y][curNode.x])
                        movesAvailable.add(moveOption);
                }
                if (movesAvailable.size() > 0) {
                    individual[i] = movesAvailable.get(r.nextInt(movesAvailable.size()));
                    curNode = new Point(curNode.x + individual[i].x, curNode.y + individual[i].y);
                } else {
                    individual[i] = curNode;
                }
            }
        }

        while (population.length > 2) {
            population = crossOver(population, moveLimit, mutationChance, hCost, lab);
        }

        Point[] a = population[fittestScoreIndex(population, hCost, moveLimit, lab)];

        Point curNode = lab.start;
        boolean[][] marked = new boolean[lab.h][lab.w];
        for (Point point : a) {
            ArrayList<Point> movesAvailable = new ArrayList<>();
            for (Point moveOption : Point.moveOptions) {
                if (lab.isValidMove(curNode.x + moveOption.x, curNode.y + moveOption.y) && !marked[curNode.y][curNode.x])
                    movesAvailable.add(moveOption);
            }
            if (movesAvailable.size() > 0) {
                lab.drawRectSTD(curNode.x, curNode.y);
                curNode = new Point(curNode.x + point.x, curNode.y + point.y);
            }
        }
    }
}
