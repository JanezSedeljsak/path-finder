from dataclasses import dataclass
import matplotlib.pyplot as plt
import math

@dataclass
class Stats:
    algorithm: str
    visitedCount: int = 0
    pathLength: int = 0
    pathPrice: int = 0
    executionTime: int = 0

def loadSingleDataset(algorithm):
    dataFromFile = [line[:-1].split(", ") for line in open(f"./results/{algorithm}.txt", 'r')][1:]
    return [Stats(
        algorithm=row[0],
        visitedCount=int(row[1]), 
        pathLength=int(row[2]), 
        pathPrice=int(row[3]), 
        executionTime=int(row[4])
    ) for row in dataFromFile]

def generateDataForAnalysis():
    return {
        "BFS": loadSingleDataset("BFS"),
        "DFS": loadSingleDataset("DFS"),
        "IDDFS": loadSingleDataset("IDDFS"),
        "AStar": loadSingleDataset("AStar"),
        "IDAStar": loadSingleDataset("IDAStar")
    }

def getGraphOptionsByAttr(attr):
    return {
        "visitedCount": ("Število obiskanih", "Število obiskanih vozlišč v labirintu"),
        "pathLength": ("Dolžina poti", "Dolžina najdene poti v labirintu"),
        "pathPrice": ("Cena poti", "Cena najdene poti v labirintu"),
        "executionTime": ("Čas izvajanja", "Čas izvajanja algoritma na posameznem labirintu"),
    }[attr]

def pathLengthGraph(dataset, attr="pathPrice", normalize=False):
    getAttr = lambda obj, atr: getattr(obj, atr) if not normalize else math.log(getattr(obj, atr))
    x, bfs, dfs, iddfs, astar, idastar = [], [], [], [], [], []
    for i in range(9):
        x.append(i+1)
        bfs.append(getAttr(dataset["BFS"][i], attr))
        dfs.append(getAttr(dataset["DFS"][i], attr))
        iddfs.append(getAttr(dataset["IDDFS"][i], attr))
        astar.append(getAttr(dataset["AStar"][i], attr))
        idastar.append(getAttr(dataset["IDAStar"][i], attr))

    yLabel, title = getGraphOptionsByAttr(attr)
    plt.plot(x, bfs, "-b", label="BFS")
    plt.plot(x, dfs, "-r", label="DFS")
    plt.plot(x, iddfs, "-g", label="IDDFS")
    plt.plot(x, astar, "-c", label="AStar")
    plt.plot(x, idastar, "-m", label="IDAStar")

    plt.legend(loc="upper left")
    plt.xlabel('#Labirint')
    plt.ylabel(yLabel)
    plt.title(title)
    return plt

def main():
    data = generateDataForAnalysis()
    plot = pathLengthGraph(data, attr="executionTime", normalize=True)
    plot.show()


if __name__ == '__main__':
    main()