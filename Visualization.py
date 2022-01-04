from dataclasses import dataclass
import matplotlib.pyplot as plt

@dataclass
class Stats:
    algorithm: str
    visitedCount: int = 0
    pathLength: int = 0
    pathPrice: int = 0

def loadSingleDataset(algorithm):
    dataFromFile = [line[:-1].split(", ") for line in open(f"./results/{algorithm}.txt", 'r')][1:]
    return [Stats(algorithm=row[0], visitedCount=int(row[1]), pathLength=int(row[2]), pathPrice=int(row[3])) for row in dataFromFile]

def generateDataForAnalysis():
    return {
        "BFS": loadSingleDataset("BFS"),
        "DFS": loadSingleDataset("DFS"),
        "IDDFS": loadSingleDataset("IDDFS")
    }

def pathLengthGraph(dataset):
    x, bfs, dfs, iddfs = [], [], [], []
    for i in range(9):
        x.append(i+1)
        bfs.append(dataset["BFS"][i].visitedCount)
        dfs.append(dataset["DFS"][i].visitedCount)
        iddfs.append(dataset["IDDFS"][i].visitedCount)

    plt.plot(x, bfs, "-b", label="BFS")
    plt.plot(x, dfs, "-r", label="DFS")
    plt.plot(x, iddfs, "-g", label="IDDFS")

    plt.legend(loc="upper left")
    plt.xlabel('#Labirint')
    plt.ylabel('Dolzina poti')
    plt.title('Dolzina poti skozi grafe')
    return plt

def main():
    data = generateDataForAnalysis()
    plot = pathLengthGraph(data)
    plot.show()


if __name__ == '__main__':
    main()