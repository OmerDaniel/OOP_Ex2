import api.GeoLocation;
import api.NodeData;

import java.util.ArrayList;

public class Node implements NodeData {
    Node next,head,tail;
    int id,tag;
    MyGeoLocation gl=new MyGeoLocation();
    String metadata;
    ArrayList<Integer> destedge;
    public void setnode (Node a,MyGeoLocation b,int ID){
        head=a;
        id=ID;
        gl=b;
        this.next=null;
    }
    public void addNode(Node n) {

        //Checks if the list is empty
        if(head == null) {
            //If list is empty, both head and tail will point to new node
            head = n;
            tail = n;
        }
        else {
            //newNode will be added after tail such that tail's next will point to newNode
            tail.next = n;
            //newNode will become new tail of the list
            tail = n;
        }
    }
    @Override
    public int getKey() {
        return this.id;
    }

    @Override
    public GeoLocation getLocation() {
        return this.gl;
    }

    @Override
    public void setLocation(GeoLocation p) {
        gl.x=p.x();
        gl.y=p.y();
        gl.z=p.z();
    }

    @Override
    public double getWeight() {
        return 0;
    }

    @Override
    public void setWeight(double w) {


    }

    @Override
    public String getInfo() {
        //"id="+this.id+" X="+this.gl.x+" y="+this.gl.y+" z="+this.gl.z
        return this.metadata;
    }

    @Override
    public void setInfo(String s) {
        this.metadata=s;

    }

    @Override
    public int getTag() {
        return this.tag;
    }

    @Override
    public void setTag(int t) {
        this.tag=t;

    }
    public Node translateToNode(NodeData n){
        Node a =new Node();
        a.id=n.getKey();
        a.metadata=n.getInfo();
        a.tag=n.getTag();
        /*
        MyGeoLocation gl=new MyGeoLocation();
        gl.x=n.getLocation().x();
        gl.y=n.getLocation().y();
        gl.z=n.getLocation().z();
        a.gl=gl;

         */
        return a;
    }
    public void maker(){
        destedge= new ArrayList<Integer>();
    }
    public Node copy (){
        Node c = new Node();
        c.id=this.id;
        c.tag=this.tag;
        c.destedge=this.destedge;
        c.gl=this.gl.copy();
        c.setInfo(this.getInfo());
        return c;
    }




}
