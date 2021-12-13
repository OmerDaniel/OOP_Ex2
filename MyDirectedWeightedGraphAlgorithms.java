import api.DirectedWeightedGraph;
import api.DirectedWeightedGraphAlgorithms;
import api.EdgeData;
import api.NodeData;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class MyDirectedWeightedGraphAlgorithms implements DirectedWeightedGraphAlgorithms {
    MyDirectedWeightedGraph graph = new MyDirectedWeightedGraph();
    @Override
    public void init(DirectedWeightedGraph g) {
        graph = graph.translate(g);
        //g=new MyDirectedWeightedGraph();
    }

    @Override
    public DirectedWeightedGraph getGraph() {
        return graph;
    }

    @Override
    public DirectedWeightedGraph copy() {
        return graph.copy();
    }

    @Override
    public boolean isConnected() {
        for (Map.Entry<Integer, Node> entry : graph.p.entrySet()){
            Node givennode=entry.getValue();
            for (Map.Entry<Integer, Node> entry1 : graph.p.entrySet()){
                Node givenode1=entry1.getValue();
                if (shortestPathDist(givennode.id,givenode1.id)>-1)
                    continue;
                else
                    return false;
            }
        }

        return true;
    }


    @Override
    public double shortestPathDist(int src, int dest) {
        return (double) dijktra(src, dest)[1];
    }
    @Override
    public List<NodeData> shortestPath(int src, int dest) {
        List<Integer> path = (List<Integer>) dijktra(src, dest)[0];
        ArrayList<NodeData> answer = new ArrayList<NodeData>();
        for (int i =0; i<path.size(); i++){
            answer.add(graph.getNode(path.get(i)));
        }
        Collections.reverse(answer);
        return answer;
    }

    public Object[] dijktra(int root, int dest) {
        int totalSize = this.graph.p.size();
        boolean[] visited = new boolean[totalSize];
        int[] gotHereBy = new int[totalSize];
        double[] gotHereWeight = new double[totalSize];

        for (int i=0; i<totalSize; i++) {
            visited[i] = false;
            gotHereBy[i] = -1;
            gotHereWeight[i] = Double.MAX_VALUE;
        }

        // Setting weight of root's path to zero, so we could start with root
        gotHereWeight[root] = 0;

        for (int unimportantIndex=0;unimportantIndex<totalSize; unimportantIndex++) {

            // Part one, we finding the unvisited node, with the least of weight \
            // weight of this path to node
            double minimumOfShortestWeight = Double.MAX_VALUE;
            // key of node
            int shortestToRootUnvisited = -1;
            for (int u=0; u<totalSize; u++) {
                if (!visited[u] && gotHereWeight[u]<minimumOfShortestWeight) {
                    minimumOfShortestWeight = gotHereWeight[u];
                    shortestToRootUnvisited = u;
                }
            }


            for (Map.Entry<Integer, MyEdgeData> entry :
                    this.graph.e.get(this.graph.p.get(shortestToRootUnvisited)).entrySet()) {
                MyEdgeData neighborEdge = entry.getValue();
                int edgeDest = neighborEdge.getDest();
                double newPossibleWeight = minimumOfShortestWeight + neighborEdge.getWeight();
                if (visited[edgeDest])
                    continue;
                if (gotHereWeight[edgeDest] <= newPossibleWeight)
                    continue;
                // the weight is less the current one and unvisited node selected
                gotHereBy[edgeDest] = shortestToRootUnvisited;
                gotHereWeight[edgeDest] = newPossibleWeight;
            }
            visited[shortestToRootUnvisited] = true;
        }

        ArrayList<Integer> pathToDest = new ArrayList<>();
        int source = dest;

        pathToDest.add(dest);
        while (source!=root) {
            source = gotHereBy[source];
            pathToDest.add(source);
        }
        Collections.reverse(pathToDest);
        return new Object[]{pathToDest, gotHereWeight[dest]};
    }

    @Override
    public NodeData center() {
        if(this.isConnected())
            return null;

        return graph.getNode(1);
    }

    @Override
    public List<NodeData> tsp(List<NodeData> cities) {
        return null;
    }

    @Override
    public boolean save(String file) {

        return false;
    }

    @Override
    public boolean load(String file) {
        DirectedWeightedGraph a = new MyDirectedWeightedGraph();
        try {
            a=Ex2.getGrapg(file);
            this.graph=graph.translate(a);
            return true;
        }catch (Error e){
            e.printStackTrace();
            return false;
        }
    }
}
