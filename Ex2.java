import api.DirectedWeightedGraph;
import api.DirectedWeightedGraphAlgorithms;
import api.EdgeData;
import api.NodeData;
import com.google.gson.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.*;
import java.util.List;

/**
 * This class is the main class for Ex2 - your implementation will be tested using this class.
 */
public class Ex2 {
    /**
     * This static function will be used to test your implementation
     * @param json_file - a json file (e.g., G1.json - G3.gson)
     * @return
     */
    public static DirectedWeightedGraph getGrapg(String json_file) {
        DirectedWeightedGraph ans = new MyDirectedWeightedGraph();
        JsonParser data = new JsonParser();
        try {
            Object o = data.parse(new FileReader(json_file));
            JsonObject json =(JsonObject) o;
            JsonArray nodes =(JsonArray) json.get("Nodes");
            for (int i =0; i<nodes.size(); i++) {
                JsonObject j = (JsonObject) nodes.get(i);
                JsonElement j1 = j.get("pos");
                JsonElement j3 = j.get("id");
                String j2 = j1.getAsString();
                String x = "";
                int m = 0;
                for (m = 0; m < j2.length(); m++) {
                    if (j2.charAt(m) == ',') {
                        m++;
                        break;
                    }
                    x += j2.charAt(m);
                }
                double ix = Double.valueOf(x);
                String y = "";
                for (int m1 = m; m < j2.length(); m1++) {
                    if (j2.charAt(m1) == ',') {
                        m1++;
                        break;
                    }
                    y += j2.charAt(m1);
                }
                double iy = Double.valueOf(y);
                Node temp = new Node();
                MyGeoLocation temp1 = new MyGeoLocation();
                temp1.x = ix;
                temp1.y = iy;
                temp1.z = 0.0;
                temp.gl = temp1;
                temp.id = j3.getAsInt();
                ans.addNode(temp);
            }
            JsonArray edges =(JsonArray) json.get("Edges");
            for (int i =0; i<edges.size(); i++) {
                JsonElement j1 = edges.get(i);
                String js = j1.toString();
                String src = "";
                int j = 0;
                for (j = 0; j < js.length(); j++) {
                    if (Character.isDigit(js.charAt(j)))
                        src += js.charAt(j);
                    if (js.charAt(j) == ',') {
                        j++;
                        break;
                    }
                }
                String w = "";
                for (int in = j; j < js.length(); in++) {
                    if (Character.isDigit(js.charAt(in)))
                        w += js.charAt(in);
                    if (js.charAt(in) == '.')
                        w += js.charAt(in);
                    if (js.charAt(in) == ',') {
                        in++;
                        j = in;
                        break;
                    }
                }
                String dest = "";
                for (int in = j; in < js.length(); in++) {
                    if (Character.isDigit(js.charAt(in)))
                        dest += js.charAt(in);
                }
                int src1 = Integer.valueOf(src);
                double w1 = Double.valueOf(w);
                int dest1 = Integer.valueOf(dest);
                ans.connect(src1, dest1, w1);

            }

        }catch (IOException e){e.printStackTrace();}

        return ans;
    }
    /**
     * This static function will be used to test your implementation
     * @param json_file - a json file (e.g., G1.json - G3.gson)
     * @return
     */
    public static DirectedWeightedGraphAlgorithms getGrapgAlgo(String json_file) {
        DirectedWeightedGraph a =getGrapg(json_file);
        MyDirectedWeightedGraph na=new MyDirectedWeightedGraph();
        na=na.translate(a);
        MyDirectedWeightedGraphAlgorithms ans = new MyDirectedWeightedGraphAlgorithms();
        ans.graph=na;
        return ans;
    }

    public static void main(String[] args) {
        System.out.println("enter a path to your json graph file:");
        Scanner scan = new Scanner(System.in);
        String json = scan.nextLine();
        runGUI(json);
        //"C:\\Users\\omer\\IdeaProjects\\Ex2\\src\\G1.json"
    }

    /**
     * This static function will run your GUI using the json fime.
     * @param json_file - a json file (e.g., G1.json - G3.gson)
     *
     */
    public static void runGUI(String json_file) {
        DirectedWeightedGraphAlgorithms alg = getGrapgAlgo(json_file);

        final JFrame frame = new JFrame();




        frame.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent componentEvent) {
            }
        });


        JMenuBar menuBar;
        JMenu menu;
        JMenuItem menuItem;

        menuBar = new JMenuBar();

        menu = new JMenu("Menu");
        menu.setMnemonic(KeyEvent.VK_A);
        menuBar.add(menu);


        final JPanel graphicPanel = (new JPanel() {

            @Override
            public void repaint(Rectangle r) {
                super.repaint(r);
            }

            @Override
            public void paintComponent(Graphics g) {
                int panelWidth = this.getWidth();
                int paddingWidth = panelWidth/10;
                int panelHeight = this.getHeight();
                int paddingHeight = panelHeight/10;
                int size = 5;

                double maxx =0;
                double minx=Double.MAX_VALUE;
                double miny=Double.MAX_VALUE;
                double maxy=0;
                int c=0;
                for(int i =0; i<alg.getGraph().nodeSize();i++){
                    if (alg.getGraph().getNode(c)==null){
                        i--;
                        c++;
                    }
                    else {
                        if (alg.getGraph().getNode(c).getLocation().x()>maxx)
                            maxx=alg.getGraph().getNode(c).getLocation().x();
                        if (alg.getGraph().getNode(c).getLocation().x()<minx)
                            minx=alg.getGraph().getNode(c).getLocation().x();
                        if (alg.getGraph().getNode(c).getLocation().y()<miny)
                            miny=alg.getGraph().getNode(c).getLocation().y();
                        if (alg.getGraph().getNode(c).getLocation().y()>maxy)
                            maxy=alg.getGraph().getNode(c).getLocation().y();
                        c++;
                    }
                }
                double absx = Math.abs(maxx-minx);
                double absy = Math.abs(maxy-miny);
                double scalex = (panelWidth - paddingWidth)/absx;
                double scaley = (panelHeight - paddingHeight)/absy;
                MyDirectedWeightedGraph myGraph = (MyDirectedWeightedGraph) alg.getGraph();
                for (Map.Entry<Integer, Node> entry : myGraph.p.entrySet()) {
                    Node node = entry.getValue();
                    if (node==null) continue;
                    int x =(int) node.getLocation().x();
                    int y = (int) node.getLocation().y();
                    g.drawOval((int) (x*scalex - minx*scalex) + (paddingWidth/2), (int) (y*scaley - miny*scaley) + (paddingHeight/2), size, size);
                }

                for (Map.Entry<NodeData, HashMap<Integer, MyEdgeData>> entry : myGraph.e.entrySet()) {
                    if (entry==null) continue;
                    for (Map.Entry<Integer, MyEdgeData> dataEntry : entry.getValue().entrySet()) {
                        if (dataEntry==null) continue;
                        EdgeData data = dataEntry.getValue();
                        double x1 = myGraph.getNode(data.getSrc()).getLocation().x();
                        double y1 = myGraph.getNode(data.getSrc()).getLocation().y();
                        double x2 = myGraph.getNode(data.getDest()).getLocation().x();
                        double y2 = myGraph.getNode(data.getDest()).getLocation().y();
                        g.drawLine(
                                (int) (x1*scalex - minx*scalex) + (paddingWidth/2),
                                (int) (y1*scaley - miny*scaley) + (paddingHeight/2),
                                (int) (x2*scalex - minx*scalex) + (paddingWidth/2),
                                (int) (y2*scaley - miny*scaley) + (paddingHeight/2)
                        );
                    }
                }
            }
        });


        menuItem = new JMenuItem("Add point", KeyEvent.VK_T);
        menuItem.addActionListener(e -> {
            JPanel panel = new JPanel();
            panel.setLayout(new GridLayout());
            TextField t1 = new TextField("x");
            TextField t2 = new TextField("y");
            TextField t3 = new TextField("id");
            panel.add(t1);
            panel.add(t2);
            panel.add(t3);
            int result = JOptionPane.showConfirmDialog(frame, panel);
            if (result == JOptionPane.OK_OPTION) {
                int x = Integer.valueOf(t1.getText());
                int y = Integer.valueOf(t1.getText());
                int id =Integer.valueOf(t3.getText());
                Node a = new Node();
                MyGeoLocation b = new MyGeoLocation();
                b.x=x;
                b.y=y;
                a.gl=b;
                a.id=id;
                alg.getGraph().addNode(a);
            }
            graphicPanel.repaint();
        });
        menu.add(menuItem);
        menuItem = new JMenuItem("Remove point", KeyEvent.VK_B);
        menuItem.addActionListener(e -> {
            JPanel panel = new JPanel();
            panel.setLayout(new GridLayout());
            TextField t1 = new TextField("point id");
            panel.add(t1);
            int result = JOptionPane.showConfirmDialog(frame, panel);
            if (result == JOptionPane.OK_OPTION){
                int id = Integer.valueOf(t1.getText());
                alg.getGraph().removeNode(id);
            }
            graphicPanel.repaint();
        });
        menu.add(menuItem);
        menuItem = new JMenuItem("Add Edge", KeyEvent.VK_D);
        menuItem.addActionListener(e -> {
            JPanel panel = new JPanel();
            panel.setLayout(new GridLayout());
            TextField t1 = new TextField("src");
            TextField t2 = new TextField("dest");
            TextField t3 = new TextField(("weight"));
            panel.add(t1);
            panel.add(t2);
            panel.add(t3);
            int result = JOptionPane.showConfirmDialog(frame, panel);
            if (result == JOptionPane.OK_OPTION){
                int src = Integer.valueOf(t1.getText());
                int dest = Integer.valueOf(t2.getText());
                int weight =Integer.valueOf(t3.getText());
                alg.getGraph().connect(src,dest,weight);
            }

            graphicPanel.repaint();
        });
        menu.add(menuItem);
        menuItem = new JMenuItem("Remove Edge", KeyEvent.VK_R);
        menuItem.addActionListener(e -> {
            JPanel panel = new JPanel();
            panel.setLayout(new GridLayout());
            TextField t1 = new TextField("src");
            TextField t2 = new TextField("dest");
            panel.add(t1);
            panel.add(t2);
            int result = JOptionPane.showConfirmDialog(frame, panel);
            if (result == JOptionPane.OK_OPTION) {
                int src = Integer.valueOf(t1.getText());
                int dest = Integer.valueOf(t2.getText());
                alg.getGraph().removeEdge(src, dest);
            }
            graphicPanel.repaint();
        });
        menu.add(menuItem);
        menu.addSeparator();
        menuItem = new JMenuItem("Shortest path", KeyEvent.VK_O);
        menuItem.addActionListener(e -> {
            JPanel panel = new JPanel();
            panel.setLayout(new GridLayout());
            TextField t1 = new TextField("src");
            TextField t2 = new TextField("dest");
            panel.add(t1);
            panel.add(t2);
            int result = JOptionPane.showConfirmDialog(frame, panel);
            if (result == JOptionPane.OK_OPTION) {
                int src = Integer.valueOf(t1.getText());
                int dest = Integer.valueOf(t2.getText());
                List<NodeData> answer = alg.shortestPath(src, dest);
                ArrayList<Integer> answer1 =new ArrayList<Integer>();
                for (int j = 0; j < answer.size(); j++) {
                    answer1.add(answer.get(j).getKey());
                }
                Collections.reverse(answer1);
                String my ="";
                for (int i =0; i<answer1.size(); i++){
                    if (i==0)
                        my+="we start from: "+answer1.get(i);
                    else
                        my+=" and then "+answer1.get(i);
                }
                JLabel label =new JLabel(my,JLabel.CENTER);
                JFrame window = new JFrame("path");
                window.setVisible(true);
                window.setSize(400,500);
                window.add(label);
            }
        });
        menu.add(menuItem);
        menuItem = new JMenuItem("shortestPathDist", KeyEvent.VK_O);
        menuItem.addActionListener(e -> {
            JPanel panel = new JPanel();
            panel.setLayout(new GridLayout());
            TextField t1 = new TextField("src");
            TextField t2 = new TextField("dest");
            panel.add(t1);
            panel.add(t2);
            int result = JOptionPane.showConfirmDialog(frame, panel);
            if (result == JOptionPane.OK_OPTION) {
                int src = Integer.valueOf(t1.getText());
                int dest = Integer.valueOf(t2.getText());
                double ans=alg.shortestPathDist(src,dest);

                String my = "the shortest path weight is: "+ans;

                JLabel label = new JLabel(my, JLabel.CENTER);
                JFrame window = new JFrame("path");
                window.setVisible(true);
                window.setSize(400, 400);
                window.add(label);
            }
        });
        menu.add(menuItem);
        menuItem = new JMenuItem("isConnected", KeyEvent.VK_O);
        menuItem.addActionListener(e -> {
            Boolean ans = alg.isConnected();
            String my = "the graph is connected";
            if (ans = false){my ="the graph is not connected";}

                JLabel label = new JLabel(my, JLabel.CENTER);
                JFrame window = new JFrame("path");
                window.setVisible(true);
                window.setSize(250, 250);
                window.add(label);


                });
        menu.add(menuItem);
        menuItem = new JMenuItem("center", KeyEvent.VK_O);
        menuItem.addActionListener(e -> {

        });
        menu.add(menuItem);
        menuItem = new JMenuItem("tsp", KeyEvent.VK_O);
        menuItem.addActionListener(e -> {

        });
        menu.add(menuItem);
        menu.addSeparator();
        menuItem = new JMenuItem("Save", KeyEvent.VK_O);
        menuItem.addActionListener(e -> {

        });
        menu.add(menuItem);
        menuItem = new JMenuItem("Load", KeyEvent.VK_O);
        menuItem.addActionListener(e -> {
            JPanel panel = new JPanel();
            panel.setLayout(new GridLayout());
            TextField t1 = new TextField("enter path for json graph");
            alg.load("");
            panel.add(t1);
            int result = JOptionPane.showConfirmDialog(frame, panel);
            if (result == JOptionPane.OK_OPTION) {
                String a=String.valueOf(t1.getText());
                runGUI(a);
            }
        });
        menu.add(menuItem);

        frame.setSize(1000,700);


        graphicPanel.repaint();

        frame.setContentPane(graphicPanel);


        frame.setVisible(true);
        frame.setJMenuBar(menuBar);

    }
}