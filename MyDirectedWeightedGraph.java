import api.DirectedWeightedGraph;
import api.EdgeData;
import api.NodeData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MyDirectedWeightedGraph implements DirectedWeightedGraph {
    HashMap<Integer,Node> p=new HashMap<Integer,Node>();
    HashMap<NodeData,HashMap<Integer,MyEdgeData>> e=new HashMap<NodeData,HashMap<Integer, MyEdgeData>>();
    private int edgecounter=0;
    @Override
    public NodeData getNode(int key) {
        return p.get(key);
    }

    @Override
    public EdgeData getEdge(int src, int dest) {
         Node nsrc=p.get(src);
             if (e.get(nsrc).containsKey(dest)) {
                 MyEdgeData my = e.get(nsrc).get(dest);
                 return my;
             }
         return null;
    }

    @Override
    public void addNode(NodeData n) {
        Node a =new Node();
        a.id=n.getKey();
        a.setInfo(n.getInfo());
        a.metadata=n.getInfo();
        a.tag=n.getTag();
        a.destedge=new ArrayList<Integer>();
        MyGeoLocation gl=new MyGeoLocation();
        gl.x=n.getLocation().x();
        gl.y=n.getLocation().y();
        gl.z=n.getLocation().z();
        a.gl=gl;
        HashMap<Integer,MyEdgeData> b=new HashMap<Integer,MyEdgeData>();
        p.put(a.id,a);
        e.put(a,b);
    }

    @Override
    public void connect(int src, int dest, double w) {
        if (e.get(p.get(src)).get(dest)!=null){
            e.get(p.get(src)).get(dest).weight=w;
        }
        else {
            MyEdgeData a = new MyEdgeData();
            a.src = src;
            a.dest = dest;
            a.weight = w;
            Node n = p.get(src);
            e.get(n).put(dest, a);
            if(p.get(dest).destedge==null)
                p.get(dest).maker();
            p.get(dest).destedge.add(src);
            edgecounter = edgecounter + 1;
        }
    }

    @Override
    public Iterator<NodeData> nodeIter() {
        Iterator itr = p.values().iterator();

        return itr;
    }

    @Override
    public Iterator<EdgeData> edgeIter() {
        Iterator itr = e.values().iterator();
        return itr;
    }

    @Override
    public Iterator<EdgeData> edgeIter(int node_id) {
        Node node=p.get(node_id);
        Iterator s=e.get(node).values().iterator();
        return s;
    }

    @Override
    public NodeData removeNode(int key) {
        ArrayList current =p.get(key).destedge;
        for(int i =0; i<current.size(); i++) {
            e.get(p.get(current.get(i))).put(key, null);
            e.get(p.get(current.get(i))).remove(key);
        }
        Node n =p.get(key);
        e.put(p.get(key),null);
        e.remove(p.get(key));
        p.put(key,null);
        return n;
    }

    @Override
    public EdgeData removeEdge(int src, int dest) {
        Node a=p.get(src);
        MyEdgeData c=e.get(a).get(dest);
        e.get(a).put(dest,null);
        edgecounter=edgecounter-1;
        return c;
    }

    @Override
    public int nodeSize() {
        int a=p.size();
        return a;
    }

    @Override
    public int edgeSize() {

        return edgecounter;
    }
    public MyDirectedWeightedGraph translate(DirectedWeightedGraph a){
        MyDirectedWeightedGraph ans = new MyDirectedWeightedGraph();
        ArrayList<Integer> nodeid=new ArrayList<Integer>();
        for (int i =0; ans.nodeSize()<a.nodeSize(); i++){
            if (a.getNode(i)==null)
                continue;
            else {
                ans.addNode(a.getNode(i));
                nodeid.add(a.getNode(i).getKey());
            }
        }
        for (int i=0; i<nodeid.size(); i++){
            for (int j=0; j<nodeid.size(); j++){
                if (i==j)
                    continue;
                if(a.getEdge(nodeid.get(i),nodeid.get(j))==null)
                    continue;
                else
                    ans.connect(nodeid.get(i),nodeid.get(j),a.getEdge(nodeid.get(i),nodeid.get(j)).getWeight());
            }
        }


        return ans;
    }

    @Override
    public int getMC() {
        return 0;
    }
    public MyDirectedWeightedGraph copy(){
        MyDirectedWeightedGraph c =new MyDirectedWeightedGraph();
        for (Map.Entry<Integer,Node> entry : p.entrySet()){
            c.addNode(entry.getValue());
        }
        for (Map.Entry<NodeData,HashMap<Integer,MyEdgeData>> entry : e.entrySet()){
            HashMap<Integer,MyEdgeData> mymap=entry.getValue();
            for (Map.Entry<Integer, MyEdgeData> entry1 : mymap.entrySet()){
                MyEdgeData a = entry1.getValue();
                c.connect(a.src,a.dest,a.weight);
            }
        }
        return c;
    }



}
/*
counter=0;
            if(a.getNode(i)==null)
                continue;
            for (int j=0; j<a.edgeSize(); j++) {
                if(a.getNode(counter)==null){
                    counter++;
                    continue;
                }
                else {
                    System.out.println(j);
                    if (counter == i) {
                        counter++;
                        continue;
                    }
                    if (a.getEdge(i, counter) == null) {
                        counter++;
                        continue;
                    } else {
                        ans.connect(i, counter, a.getEdge(i, counter).getWeight());
                        counter++;
                    }
                }
            }
 */