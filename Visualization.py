from dataclasses import dataclass
import matplotlib.pyplot as plt

@dataclass
class SignleAlgo:
    algorithm: str
    visitedCount: int = 0
    pathLength: int = 0
    pathPrice: int = 0

@dataclass
class AlgoContainer:
    BFS: SignleAlgo
    DFS: SignleAlgo
    IDDFS: SignleAlgo

def loadSingleDataset(labNum):
    return [line[:-1].split(", ") for line in open(f"./results/labyrinth_{labNum}.txt", 'r')][1:]

def generateAlgoStruct(labNum):
    data = []
    for row in loadSingleDataset(labNum):
        data.append(SignleAlgo(
            algorithm=row[0], 
            visitedCount=int(row[1]), 
            pathLength=int(row[2]), 
            pathPrice=int(row[3])
        ))
    
    return AlgoContainer(BFS=data[0],DFS=data[1],IDDFS=data[2])

def loadDatasets():
    return [generateAlgoStruct(i) for i in range(1, 10)]

def pathLengthGraph(dataset):
    x, bfs, dfs, iddfs = [], [], [], []
    for (i, data) in enumerate(dataset):
        x.append(i)
        bfs.append(data.BFS.pathLength)
        dfs.append(data.DFS.pathLength)
        iddfs.append(data.IDDFS.pathLength)

    plt.plot(x, bfs, "-b", label="BFS")
    plt.plot(x, dfs, "-r", label="DFS") 
    plt.plot(x, iddfs, "-g", label="IDDFS")

    plt.legend(loc="upper left")
    plt.xlabel('#Labirint')
    plt.ylabel('Dolzina poti')
    plt.title('Dolzina poti skozi grafe')
    return plt

def main():
    data = loadDatasets()
    plot = pathLengthGraph(data)
    plot.show()


if __name__ == '__main__':
    main()