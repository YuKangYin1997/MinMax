import ca.uottawa.csi5318.node.MinMaxPlusNode;
import io.jbotsim.core.Link;
import io.jbotsim.core.Point;
import io.jbotsim.core.Topology;
import io.jbotsim.ui.JViewer;

public class MinMaxPlusView {

    public static void main(String[] args) {
        Topology tp = new Topology();
        tp.setDefaultNodeModel(MinMaxPlusNode.class);

        // hardcode nodes distribution
        // example 1
        // 80 -> 49 -> 17 -> 35 -> 52
        // |                        |
        // 47 <- 29 <- 88 <- 40 <- 54
        // example 2
        // 160 -> 416 -> 113 -> 377
        // |                     |
        // 351 <- 427 <- 442 <- 762
        MinMaxPlusNode node80 = new MinMaxPlusNode(1);
        node80.setID(80);
        node80.setLocation(new Point(160, 100, 0));

        MinMaxPlusNode node49 = new MinMaxPlusNode(1);
        node49.setID(49);
        node49.setLocation(new Point(210, 100, 0));

        MinMaxPlusNode node17 = new MinMaxPlusNode(1);
        node17.setID(17);
        node17.setLocation(new Point(260, 100, 0));

        MinMaxPlusNode node35 = new MinMaxPlusNode(1);
        node35.setID(35);
        node35.setLocation(new Point(310, 100, 0));

        MinMaxPlusNode node52 = new MinMaxPlusNode(1);
        node52.setID(52);
        node52.setLocation(new Point(360, 100, 0));

        MinMaxPlusNode node54 = new MinMaxPlusNode(1);
        node54.setID(54);
        node54.setLocation(new Point(360, 200, 0));

        MinMaxPlusNode node40 = new MinMaxPlusNode(1);
        node40.setID(40);
        node40.setLocation(new Point(310, 200, 0));

        MinMaxPlusNode node88 = new MinMaxPlusNode(1);
        node88.setID(88);
        node88.setLocation(new Point(260, 200, 0));

        MinMaxPlusNode node29 = new MinMaxPlusNode(1);
        node29.setID(29);
        node29.setLocation(new Point(210, 200, 0));

        MinMaxPlusNode node47 = new MinMaxPlusNode(1);
        node47.setID(47);
        node47.setLocation(new Point(160, 200, 0));

        // add all nodes to topology
        tp.addNode(node80);
        tp.addNode(node49);
        tp.addNode(node17);
        tp.addNode(node35);
        tp.addNode(node52);
        tp.addNode(node54);
        tp.addNode(node40);
        tp.addNode(node88);
        tp.addNode(node29);
        tp.addNode(node47);

        // link nodes with one another in DIRECTED orientation
        tp.addLink(new Link(node80, node49, Link.Orientation.DIRECTED));
        tp.addLink(new Link(node49, node17, Link.Orientation.DIRECTED));
        tp.addLink(new Link(node17, node35, Link.Orientation.DIRECTED));
        tp.addLink(new Link(node35, node52, Link.Orientation.DIRECTED));
        tp.addLink(new Link(node52, node54, Link.Orientation.DIRECTED));
        tp.addLink(new Link(node54, node40, Link.Orientation.DIRECTED));
        tp.addLink(new Link(node40, node88, Link.Orientation.DIRECTED));
        tp.addLink(new Link(node88, node29, Link.Orientation.DIRECTED));
        tp.addLink(new Link(node29, node47, Link.Orientation.DIRECTED));
        tp.addLink(new Link(node47, node80, Link.Orientation.DIRECTED));

        tp.disableWireless();
        new JViewer(tp);
        tp.start();

        // select all nodes as initiators
        tp.selectNode(node80);
        tp.selectNode(node49);
        tp.selectNode(node17);
        tp.selectNode(node35);
        tp.selectNode(node52);
        tp.selectNode(node54);
        tp.selectNode(node40);
        tp.selectNode(node88);
        tp.selectNode(node29);
        tp.selectNode(node47);
    }

}
