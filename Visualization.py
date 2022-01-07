from dataclasses import dataclass
import matplotlib.pyplot as plt
import math

@dataclass
class Stats:
    algorithm: str
    visitedCount: int = 0
    hallwayLength: int = 0
    pathLength: int = 0
    pathPrice: int = 0
    executionTime: int = 0

    visitedPercentage: float = .0
    pathLengthBasedOnHallway: float = .0
    pathPriceBasedOnHallway: float = .0

def loadSingleDataset(algorithm):
    dataFromFile = [line[:-1].split(", ") for line in open(f"./results/{algorithm}.txt", 'r')][1:]
    return [Stats(
        algorithm=row[0],
        visitedCount=int(row[1]), 
        hallwayLength=int(row[2]),
        pathLength=int(row[3]), 
        pathPrice=int(row[4]),
        executionTime=int(row[5]),

        visitedPercentage=int(row[1]) / int(row[2]),
        pathLengthBasedOnHallway=int(row[3]) / int(row[2]),
        pathPriceBasedOnHallway=int(row[4]) / int(row[2])
    ) for row in dataFromFile]

def generateDataForAnalysis():
    return {
        "BFS": loadSingleDataset("BFS"),
        "DFS": loadSingleDataset("DFS"),
        "IDDFS": loadSingleDataset("IDDFS"),
        "AStar": loadSingleDataset("AStar"),
        "AStarWeighted": loadSingleDataset("AStarWeighted"),
        "IDAStar": loadSingleDataset("IDAStar"),
        "GBFS": loadSingleDataset("GBFS"),
        "SADFS": loadSingleDataset("SADFS")
    }

def getGraphOptionsByAttr(attr):
    return {
        "visitedPercentage": ("Delež obiskanih vozlišč (%)", "Delež obiskanih vozlišč (hodnika) v labirintu"),
        "pathLengthBasedOnHallway": ("Dolžina poti / Dolžina hodnika", "Dolžina najdene poti v razmerju z dolžino hodnika v labirintu"),
        "pathPriceBasedOnHallway": ("Cena poti / Dolžina hodnika", "Cena poti v razmerju z dolžino hodnika v labirintu"),
        "pathLength": ("Dolžina poti (stevilo vozlišč)", "Dolžina najdene poti v labirintu"),
        "pathPrice": ("Cena poti", "Cena najdene poti v labirintu"),
        "executionTime": ("Čas izvajanja (ns)", "Čas izvajanja algoritma na posameznem labirintu"),
    }[attr]

def drawGraph(dataset, attr="pathPrice", normalize=False):
    getAttr = lambda obj, atr: getattr(obj, atr) if not normalize else math.log(getattr(obj, atr))
    x, bfs, dfs, iddfs, gbfs, astar, astarw, idastar, sadfs = [], [], [], [], [], [], [], [], []
    for i in range(9):
        x.append(i+1)
        bfs.append(getAttr(dataset["BFS"][i], attr))
        dfs.append(getAttr(dataset["DFS"][i], attr))
        iddfs.append(getAttr(dataset["IDDFS"][i], attr))
        gbfs.append(getAttr(dataset["GBFS"][i], attr))
        astar.append(getAttr(dataset["AStar"][i], attr))
        astarw.append(getAttr(dataset["AStarWeighted"][i], attr))
        idastar.append(getAttr(dataset["IDAStar"][i], attr))
        sadfs.append(getAttr(dataset["SADFS"][i], attr))

    yLabel, title = getGraphOptionsByAttr(attr)
    plt.plot(x, bfs, "red", label="BFS")
    plt.plot(x, dfs, "blue", label="DFS")
    plt.plot(x, iddfs, "green", label="IDDFS")
    plt.plot(x, gbfs, "purple", label="GBFS")
    plt.plot(x, astar, "lime", label="AStar")
    plt.plot(x, astar, "cyan", label="AStarWeighted")
    plt.plot(x, idastar, "magenta", label="IDAStar")
    plt.plot(x, sadfs, "orange", label="SADFS")

    plt.legend(loc="lower left" if attr == 'visitedPercentage' else "upper left")
    plt.xlabel('#Labirint')
    plt.ylabel(yLabel)
    plt.title(title)
    return plt

def drawTable(data, labNum):
    rows = []
    for data in data.values():
        section = data[labNum-1]
        rows.append([
            section.algorithm, 
            section.visitedCount, 
            section.pathLength, 
            section.pathPrice, 
            section.executionTime
        ])

    fig, ax = plt.subplots()
    fig.patch.set_visible(False)
    ax.axis('off')

    cols = ["algorithm", "visitedCount", "pathLength", "pathPrice", "executionTime"]
    ax.table(cellText=rows, colLabels=cols, loc='center')

    return plt


def main():
    data = generateDataForAnalysis()
    plot = drawTable(data, 3)
    plot.show()


if __name__ == '__main__':
    main()