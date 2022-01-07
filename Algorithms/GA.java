package Algorithms;

import Core.Labyrinth;
import Core.Point;
import Core.Solution;

import java.util.Random;

public class GA {
    static int fitness(Point[] individual, Labyrinth lab, int[][] hCost) {
        Point curNode = lab.start;
        boolean[][] marked = new boolean[lab.h][lab.w];

        for (Point point : individual) {
            Point nextNode = new Point(curNode.x + point.x, curNode.y + point.y);
            if (lab.isValidMove(nextNode.x, nextNode.y) && !marked[nextNode.y][nextNode.x]) {
                marked[curNode.y][curNode.x] = true;
                curNode = nextNode;
            }
        }

        return hCost[curNode.y][curNode.x];
    }

    static int[] scoreIndices(Point[][] population, int[][] hCost, Labyrinth lab) {
        int bestIndex1 = 1;
        int bestIndex2 = 0;
        int worstIndex1 = 1;
        int worstIndex2 = 0;

        for (int i = 1; i < population.length; i++) {
            if (fitness(population[i], lab, hCost) > fitness(population[bestIndex1], lab, hCost)) {
                bestIndex2 = bestIndex1;
                bestIndex1 = i;
            }
            if (fitness(population[i], lab, hCost) < fitness(population[worstIndex1], lab, hCost)) {
                worstIndex2 = worstIndex1;
                worstIndex1 = i;
            }

        }
        return new int[] {bestIndex1, bestIndex2, worstIndex1, worstIndex2};
    }

    static void mutate(Point[] individual, double mutationChance) {
        Random r = new Random();
        for (int i = 0; i < individual.length; i++) {
            if (mutationChance > Math.random()) {
                individual[i] = Point.moveOptions[r.nextInt(4)];
            }
        }
    }

    static Point[] crossOver(Point[] parent1, Point[] parent2, int moveLimit) {
        Random r = new Random();
        Point[] child = new Point[moveLimit];
        int crossLimit = r.nextInt(moveLimit - 1);

        for (int i = 0; i < moveLimit; i++) {
            if (i < crossLimit)
                child[i] = parent1[i];
            else
                child[i] = parent2[i];
        }

        return child;
    }

    public static void fullSearch(Labyrinth lab) {
        int moveLimit = 200;
        int populationSize = 500;
        int generationNumber = 5000;
        double mutationChance = 0.5;
        Random r = new Random();

        Solution.reset(lab.h, lab.w);

        int[][] hCost = Labyrinth.hScoreGrid(lab, lab.end, 1f);
        Point[][] population = new Point[populationSize][moveLimit];

        // fill 1st population with random moves
        for (int i = 0; i < populationSize; i++) {
            Point[] path = new Point[moveLimit];
            for (int j = 0; j < moveLimit; j++) {
                path[j] = Point.moveOptions[r.nextInt(4)];
            }
            population[i] = path;
        }

        for (int i = 0; i < generationNumber; i++) {
            System.out.println("Generacija " + i);

            // mutate population
            if (i < generationNumber - generationNumber / 10)
                for (Point[] individual : population) {
                    mutate(individual, mutationChance);
                }

            // get two best individuals
            int[] indices = scoreIndices(population, hCost, lab);
            Point[] best1 = population[indices[0]];
            Point[] best2 = population[indices[1]];

            Point[] child1 = crossOver(best1, best2, moveLimit);
            Point[] child2 = crossOver(best2, best1, moveLimit);

            // get two worst individuals and replace them with children of best
            population[indices[2]] = child1;
            population[indices[3]] = child2;
        }

        // get best individual
        int[] indices = scoreIndices(population, hCost, lab);

        Point curNode = lab.start;
        boolean[][] marked = new boolean[lab.h][lab.w];

        for (Point point : population[indices[0]]) {
            Point nextNode = new Point(curNode.x + point.x, curNode.y + point.y);
            if (lab.isValidMove(nextNode.x, nextNode.y) && !marked[nextNode.y][nextNode.x]) {
                lab.drawRectSTD(curNode.x, curNode.y);
                marked[curNode.y][curNode.x] = true;
                curNode = nextNode;
            }
        }
    }
}
