import api.EdgeData;

public class MyEdgeData implements EdgeData {
    int src, dest, tag;
    double weight;
    String metadata="";

    public void MyEdgeData(int s, int d, double w) {
        src = s;
        dest = d;
        weight = w;
    }

    @Override
    public int getSrc() {
        return this.src;
    }

    @Override
    public int getDest() {
        return this.dest;
    }

    @Override
    public double getWeight() {
        return this.weight;
    }

    @Override
    public String getInfo() {
        return this.metadata;
    }

    @Override
    public void setInfo(String s) {
        this.metadata = s;

    }

    @Override
    public int getTag() {
        return this.tag;
    }

    @Override
    public void setTag(int t) {
        this.tag = t;


    }


}