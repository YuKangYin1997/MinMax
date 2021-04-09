package ca.uottawa.csi5318.node;

import ca.uottawa.csi5318.message.MinMaxMessage;
import ca.uottawa.csi5318.util.MessageType;
import ca.uottawa.csi5318.util.NodeStatus;
import io.jbotsim.core.Color;
import io.jbotsim.core.Message;
import io.jbotsim.core.Node;

public class MinMaxNode extends Node {

    private String status;
    private int stage;
    private static int messageComplexity = 0;

    public MinMaxNode(int stage) {
        this.stage = stage;
        this.status = NodeStatus.ASLEEP;
    }

    private Node getSuccessorNode() {
        return this.getOutLinks().get(0).endpoint(1);
    }

    @Override
    public void send(Node destination, Message message) {
        super.send(destination, message);
        messageComplexity++;
    }

    @Override
    public void onStart() {
        System.out.println("Node " + this.getID() + " has been initialized with status " + this.status);
    }

    @Override
    public void onSelection() {
        System.out.println("Node " + this.getID() + " has been selected as an initiator.");

        this.status = NodeStatus.CANDIDATE;
        setColor(Color.YELLOW);
        MinMaxMessage minMaxMessage = new MinMaxMessage(MessageType.ENVELOP, this.getID(), this.stage);
        send(this.getSuccessorNode(), new Message(minMaxMessage));
        System.out.println("Node " + this.getID() + " (" + this.status + ") sends a message " + minMaxMessage + ", now MS is " + messageComplexity);
    }

    @Override
    public void onMessage(Message message) {
        MinMaxMessage minMaxMessage = (MinMaxMessage) message.getContent();
        System.out.println("Node " + this.getID() + " (" + this.status + ") receives a message: " + minMaxMessage);

        switch (this.status) {
            case NodeStatus.ASLEEP:
                // become defeated and just forward received message to next node
                send(getSuccessorNode(), message);
                this.status = NodeStatus.DEFEATED;
                setColor(Color.BLACK);
                System.out.println("Node " + this.getID() + " becomes " + this.status + " and forward message " + minMaxMessage);
                break;
            case NodeStatus.CANDIDATE:
                if (this.getID() == minMaxMessage.getValue()) {  // send notify and become leader
                    send(getSuccessorNode(),
                            new Message(new MinMaxMessage(MessageType.NOTIFY, this.getID(), this.stage)));
                    this.status = NodeStatus.LEADER;
                    setColor(Color.RED);
                    System.out.println("Node " + this.getID() + " becomes the Leader and sends a NOTIFY message, now MS is " + messageComplexity);
                } else {
                    processEnvelop(message);
                }
                break;
            case NodeStatus.DEFEATED:
                if (minMaxMessage.getType().equals(MessageType.ENVELOP)) {  // forward this message to next node
                    send(getSuccessorNode(), message);
                    System.out.println("Node " + this.getID() + " (" + this.status + ") forwards a message " + minMaxMessage);
                } else {  // received message type is notify, send notify and become follower
                    MinMaxMessage newMinMaxMessage = new MinMaxMessage(MessageType.NOTIFY, -1, -1);
                    send(getSuccessorNode(), new Message(newMinMaxMessage));
                    this.status = NodeStatus.FOLLOWER;
                    setColor(Color.GREEN);
                    System.out.println("Node " + this.getID() + " becomes " + this.status + " and forward NOTIFY message");
                }
                break;
            case NodeStatus.LEADER:  // the Leader node receives the NOTIFY message again
                System.out.println("\nNode " + this.getID() + " is the Leader");
                System.out.println("Current Stage Number = " + this.stage);
                System.out.println("Message Complexity = " + messageComplexity);
                break;
            case NodeStatus.FOLLOWER:
                throw new RuntimeException("Node " + this.getID() + " status is FOLLOWER and it shouldn't received a message again!");
            default:
                throw new RuntimeException("Invalid node status [" + this.status + "]");
        }
    }

    private void processEnvelop(Message message) {
        MinMaxMessage minMaxMessage = (MinMaxMessage) message.getContent();
        int stage = minMaxMessage.getStage();
        int value = minMaxMessage.getValue();
        if (stage % 2 == 1) { // odd stage
            if (value < this.getID()) {  // keep the received value
                this.stage += 1;  // update current stage
                int previousID = this.getID();
                this.setID(value);  // update value/ID
                MinMaxMessage newMinMaxMessage = new MinMaxMessage(MessageType.ENVELOP, this.getID(), this.stage);
                send(getSuccessorNode(), new Message(newMinMaxMessage));
                System.out.println("Node " + previousID + "(" + this.status + ")" + " updates ID to " + this.getID() + " and sends a message " + newMinMaxMessage + ", now MS is " + messageComplexity);
            } else {  // discard the received value and become defeated
                this.status = NodeStatus.DEFEATED;
                setColor(Color.BLACK);
                System.out.println("Node " + this.getID() + " becomes DEFEATED after receiving " + minMaxMessage);
            }
        } else {  // even stage
            if (value > this.getID()) {  // keep the received value
                this.stage += 1;  // update current stage
                int previousID = this.getID();
                this.setID(value);  // update value/ID
                MinMaxMessage newMinMaxMessage = new MinMaxMessage(MessageType.ENVELOP, this.getID(), this.stage);
                send(getSuccessorNode(), new Message(newMinMaxMessage));
                System.out.println("Node " + previousID + "(" + this.status + ")" + " updates ID to " + this.getID() + " and sends a message " + newMinMaxMessage + ", now MS is " + messageComplexity);
            } else {  // discard the received value and become defeated
                this.status = NodeStatus.DEFEATED;
                setColor(Color.BLACK);
                System.out.println("Node " + this.getID() + " becomes DEFEATED after receiving " + minMaxMessage);
            }
        }
    }
}
