package ca.uottawa.csi5318.algorithm;

import ca.uottawa.csi5318.node.MinMaxPlusNode;
import io.jbotsim.core.Link;
import io.jbotsim.core.Node;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MinMaxPlus {

    private List<Node> nodes;
    private List<Link> links;

    public List<Node> getNodes() {
        return nodes;
    }

    public List<Link> getLinks() {
        return links;
    }

    public MinMaxPlus(int minNodeValue, int maxNodeValue, int amount) {
        if (maxNodeValue - minNodeValue + 1 < amount) {
            throw new RuntimeException("Can't generate " + amount + " random nodes, please reset minNodeValue and maxNodeValue");
        }
        if (minNodeValue > maxNodeValue) {
            throw new RuntimeException("minNodeValue is larger than maxNodeValue, please reset minNodeValue and maxNodeValue");
        }
        this.nodes = generateRandomNodes(minNodeValue, maxNodeValue, amount);
        this.links = linkNodes(this.nodes);
        System.out.println("Generate " + amount + " random nodes and link them with one another");
    }

    /**
     * get initial random distributions of nodes
     * @param minNodeValue
     * @param maxNodeValue
     * @param amount
     * @return
     */
    private List<Node> generateRandomNodes(int minNodeValue, int maxNodeValue, int amount) {
        HashSet<Integer> set = new HashSet<>();
        List<Node> nodes = new ArrayList<>();
        int range = maxNodeValue - minNodeValue + 1;
        while (set.size() != amount) {
            int rand = (int)(Math.random() * range) + minNodeValue;
            set.add(rand);
        }
        for (Integer nodeID : set) {
            MinMaxPlusNode minMaxPlusNode = new MinMaxPlusNode(1);
            minMaxPlusNode.setID(nodeID);
            nodes.add(minMaxPlusNode);
        }
        return nodes;
    }

    /**
     * links nodes with one another in directed orientation
     * @param nodes
     * @return
     */
    private List<Link> linkNodes(List<Node> nodes) {
        List<Link> links = new ArrayList<>();
        Node head = nodes.get(0);
        Node tail = nodes.get(nodes.size() - 1);
        for (int i = 0; i < nodes.size() - 1; ++i) {
            links.add(new Link(nodes.get(i), nodes.get(i + 1), Link.Orientation.DIRECTED));
        }
        links.add(new Link(tail, head, Link.Orientation.DIRECTED));
        return links;
    }

    public List<Node> getInitiatorNodes() {
        List<Node> initiators = new ArrayList<>();
        int amount = (int) (Math.random() * nodes.size()) + 1;
        Set<Integer> set = new HashSet<>();
        int range = nodes.size();

        while (set.size() != amount) {
            int rand = (int)(Math.random() * range);
            set.add(rand);
        }

        for (Integer index : set) {
            initiators.add(nodes.get(index));
        }
        System.out.println("Initiator nodes: " + initiators);
        return initiators;
    }
}
