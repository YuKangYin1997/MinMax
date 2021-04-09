import ca.uottawa.csi5318.node.MinMaxNode;
import io.jbotsim.core.Link;
import io.jbotsim.core.Point;
import io.jbotsim.core.Topology;
import io.jbotsim.ui.JViewer;


public class MinMaxView {

    public static void main(String[] args) {
        Topology tp = new Topology();
        tp.setDefaultNodeModel(MinMaxNode.class);

        // hardcode nodes distribution
        // 11 -> 10 -> 20
        // |           |
        // 13 <- 22 <- 9
        MinMaxNode node11 = new MinMaxNode(1);
        node11.setID(11);
        node11.setLocation(new Point(160, 100, 0));

        MinMaxNode node10 = new MinMaxNode(1);
        node10.setID(10);
        node10.setLocation(new Point(210, 100, 0));

        MinMaxNode node20 = new MinMaxNode(1);
        node20.setID(20);
        node20.setLocation(new Point(260, 100, 0));

        MinMaxNode node9 = new MinMaxNode(1);
        node9.setID(9);
        node9.setLocation(new Point(260, 150, 0));

        MinMaxNode node22 = new MinMaxNode(1);
        node22.setID(22);
        node22.setLocation(new Point(210, 150, 0));

        MinMaxNode node13 = new MinMaxNode(1);
        node13.setID(13);
        node13.setLocation(new Point(160, 150, 0));

        // add all nodes to topology
        tp.addNode(node11);
        tp.addNode(node10);
        tp.addNode(node20);
        tp.addNode(node9);
        tp.addNode(node22);
        tp.addNode(node13);

        // link nodes with one another in DIRECTED orientation
        tp.addLink(new Link(node11, node10, Link.Orientation.DIRECTED));
        tp.addLink(new Link(node10, node20, Link.Orientation.DIRECTED));
        tp.addLink(new Link(node20, node9, Link.Orientation.DIRECTED));
        tp.addLink(new Link(node9, node22, Link.Orientation.DIRECTED));
        tp.addLink(new Link(node22, node13, Link.Orientation.DIRECTED));
        tp.addLink(new Link(node13, node11, Link.Orientation.DIRECTED));

        tp.disableWireless();
        new JViewer(tp);
        tp.start();

        // select all nodes as initiators
        tp.selectNode(node11);
        tp.selectNode(node10);
        tp.selectNode(node20);
        tp.selectNode(node9);
        tp.selectNode(node22);
        tp.selectNode(node13);
    }
}
