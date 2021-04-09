import ca.uottawa.csi5318.algorithm.MinMax;
import ca.uottawa.csi5318.node.MinMaxNode;
import io.jbotsim.core.Link;
import io.jbotsim.core.Node;
import io.jbotsim.core.Topology;
import io.jbotsim.ui.JViewer;

import java.util.List;

public class MinMaxTest {

    public static void main(String[] args) {
        MinMax minMax = new MinMax(1, 1000, 110);
        Topology tp = new Topology();
        tp.setDefaultNodeModel(MinMaxNode.class);

        List<Node> nodes = minMax.getNodes();
        for (Node node : nodes) {
            tp.addNode(node);
        }

        List<Link> links = minMax.getLinks();
        for (Link link : links) {
            tp.addLink(link);
        }

        tp.disableWireless();
        new JViewer(tp);
        tp.start();

//        List<Node> initiatorNodes = minMax.getInitiatorNodes();  // select random nodes as initiators
        List<Node> initiatorNodes = minMax.getNodes();  // select all nodes as initiators
        for (Node initiatorNode : initiatorNodes) {
            tp.selectNode(initiatorNode);
        }
    }
}
