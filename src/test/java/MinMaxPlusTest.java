import ca.uottawa.csi5318.algorithm.MinMaxPlus;
import ca.uottawa.csi5318.node.MinMaxPlusNode;
import io.jbotsim.core.Link;
import io.jbotsim.core.Node;
import io.jbotsim.core.Topology;
import io.jbotsim.ui.JViewer;

import java.util.List;

public class MinMaxPlusTest {

    public static void main(String[] args) {
        MinMaxPlus minMaxPlus = new MinMaxPlus(1, 1000, 70);
        Topology tp = new Topology();
        tp.setDefaultNodeModel(MinMaxPlusNode.class);

        List<Node> nodes = minMaxPlus.getNodes();
        for (Node node : nodes) {
            tp.addNode(node);
        }

        List<Link> links = minMaxPlus.getLinks();
        for (Link link : links) {
            tp.addLink(link);
        }

        tp.disableWireless();
        new JViewer(tp);
        tp.start();

//        List<Node> initiatorNodes = minMaxPlus.getInitiatorNodes();  // select all nodes as initiators
        List<Node> initiatorNodes = minMaxPlus.getNodes();  // select all nodes as initiators
        for (Node initiatorNode : initiatorNodes) {
            tp.selectNode(initiatorNode);
        }
    }

}
