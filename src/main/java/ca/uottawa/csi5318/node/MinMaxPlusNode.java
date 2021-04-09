package ca.uottawa.csi5318.node;

import ca.uottawa.csi5318.message.MinMaxPlusMessage;
import ca.uottawa.csi5318.util.FibonacciSequence;
import ca.uottawa.csi5318.util.MessageType;
import ca.uottawa.csi5318.util.NodeStatus;
import io.jbotsim.core.Color;
import io.jbotsim.core.Message;
import io.jbotsim.core.Node;

public class MinMaxPlusNode extends Node {

    private String status;
    private int stage;
    private static int messageComplexity = 0;

    public MinMaxPlusNode(int stage) {
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
        MinMaxPlusMessage minMaxPlusMessage = new MinMaxPlusMessage(MessageType.ENVELOP, this.getID(), this.stage, Integer.MAX_VALUE);
        send(this.getSuccessorNode(), new Message(minMaxPlusMessage));
        System.out.println("Node " + this.getID() + " (" + this.status + ") sends a message " + minMaxPlusMessage + ", now MS is " + messageComplexity);
    }

    @Override
    public void onMessage(Message message) {
        MinMaxPlusMessage minMaxPlusMessage = (MinMaxPlusMessage) message.getContent();
        System.out.println("Node " + this.getID() + " (" + this.status + ") receives a message: " + minMaxPlusMessage);

        switch (this.status) {
            case NodeStatus.ASLEEP:
                send(getSuccessorNode(), message);
                this.status = NodeStatus.DEFEATED;
                setColor(Color.BLACK);
                System.out.println("Node " + this.getID() + " becomes " + this.status + " and forward message " + minMaxPlusMessage);
                break;
            case NodeStatus.CANDIDATE:
                if (this.getID() == minMaxPlusMessage.getValue()) {
                    send(getSuccessorNode(),
                            new Message(new MinMaxPlusMessage(MessageType.NOTIFY, this.getID(), this.stage, -1)));
                    this.status = NodeStatus.LEADER;
                    setColor(Color.RED);
                    System.out.println("Node " + this.getID() + " becomes the Leader and sends a NOTIFY message, now MS is " + messageComplexity);
                } else {
                    processEnvelop(message);
                }
                break;
            case NodeStatus.DEFEATED:
                String receivedType = minMaxPlusMessage.getType();
                if (receivedType.equals(MessageType.NOTIFY)) {
                    send(getSuccessorNode(), message);  // forward notify message
                    this.status = NodeStatus.FOLLOWER;
                    setColor(Color.GREEN);
                    System.out.println("Node " + this.getID() + " becomes " + this.status + " and forward NOTIFY message");
                } else {  // received message is "ENVELOP" type
                    int receivedStage = minMaxPlusMessage.getStage();
                    int receivedValue = minMaxPlusMessage.getValue();
                    int receivedDistance = minMaxPlusMessage.getDistance();

                    if (receivedStage % 2 == 0) {  // received message is in even stage
                        if (receivedDistance > 0) {
                            receivedDistance = receivedDistance - 1;
                            MinMaxPlusMessage newMinMaxPlusMessage = new MinMaxPlusMessage(MessageType.ENVELOP, receivedValue, receivedStage, receivedDistance);
                            send(getSuccessorNode(), new Message(newMinMaxPlusMessage));
                            System.out.println("Node " + this.getID() + " (" + this.status + ") decrease the distance in received message by 1 and forwards it " + newMinMaxPlusMessage);
                        } else if (receivedDistance == 0) {
                            this.stage = receivedStage + 1;
                            int oldID = this.getID();
                            this.setID(receivedValue);
                            this.status = NodeStatus.CANDIDATE;
                            setColor(Color.YELLOW);
                            MinMaxPlusMessage newMinMaxPlusMessage = new MinMaxPlusMessage(MessageType.ENVELOP, this.getID(), this.stage, Integer.MAX_VALUE);
                            send(getSuccessorNode(), new Message(newMinMaxPlusMessage));
                            System.out.println("Node " + oldID + " updates value to " + this.getID() + " and becomes " + this.status + " and send a message: " + newMinMaxPlusMessage);
                        } else {
                            throw new RuntimeException("Distance shouldn't be less than 0");
                        }
                    } else {  // received message in in odd stage
                        if (receivedStage - this.stage == 1 && receivedValue < this.getID()) {
                            this.stage = receivedStage + 1;
                            int oldID = this.getID();
                            this.setID(receivedValue);
                            this.status = NodeStatus.CANDIDATE;
                            setColor(Color.YELLOW);
                            MinMaxPlusMessage newMinMaxPlusMessage = new MinMaxPlusMessage(MessageType.ENVELOP, this.getID(), this.stage, FibonacciSequence.get(this.stage + 2));
                            send(getSuccessorNode(), new Message(newMinMaxPlusMessage));
                            System.out.println("Node " + oldID + " updates value to " + this.getID() + " and becomes " + this.status + " and send a message: " + newMinMaxPlusMessage);
                        } else {
                            receivedDistance = receivedDistance - 1;
                            MinMaxPlusMessage newMinMaxPlusMessage = new MinMaxPlusMessage(MessageType.ENVELOP, receivedValue, receivedStage, receivedDistance);
                            send(getSuccessorNode(), new Message(newMinMaxPlusMessage));
                            System.out.println("Node " + this.getID() + " (" + this.status + ") forwards received message " + minMaxPlusMessage);
                        }
                    }
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
        MinMaxPlusMessage minMaxPlusMessage = (MinMaxPlusMessage) message.getContent();
        int stage = minMaxPlusMessage.getStage();
        int value = minMaxPlusMessage.getValue();
        if (stage % 2 == 1) {  // odd stage
            if (stage - this.stage == 1) {
                this.status = NodeStatus.DEFEATED;
                setColor(Color.BLACK);
                send(getSuccessorNode(), message);
                System.out.println("Node " + this.getID() + " become DEFEATED after receiving a message from higher stage: " + minMaxPlusMessage + " and forward this message");
            } else if (value < this.getID()) {
                this.stage += 1;
                int oldID = this.getID();
                this.setID(value);
                MinMaxPlusMessage newMinMaxPlusMessage = new MinMaxPlusMessage(MessageType.ENVELOP, this.getID(), this.stage, FibonacciSequence.get(this.stage + 2));
                send(getSuccessorNode(), new Message(newMinMaxPlusMessage));
                System.out.println("Node " + oldID + "(" + this.status + ")" + " updates ID to " + this.getID() + " and sends a message " + newMinMaxPlusMessage + ", now MS is " + messageComplexity);
            } else {
                this.status = NodeStatus.DEFEATED;
                setColor(Color.BLACK);
                System.out.println("Node " + this.getID() + " becomes DEFEATED after receiving " + minMaxPlusMessage);
            }
        } else {
            if (stage - this.stage == 1) {
                this.status = NodeStatus.DEFEATED;
                setColor(Color.BLACK);
                send(getSuccessorNode(), message);
                System.out.println("Node " + this.getID() + " become DEFEATED after receiving a message from higher stage: " + minMaxPlusMessage + " and forward this message");
            } else if (value > this.getID()) {
                this.stage += 1;
                int oldID = this.getID();
                this.setID(value);
                MinMaxPlusMessage newMinMaxPlusMessage = new MinMaxPlusMessage(MessageType.ENVELOP, this.getID(), this.stage, Integer.MAX_VALUE);
                send(getSuccessorNode(), new Message(newMinMaxPlusMessage));
                System.out.println("Node " + oldID + "(" + this.status + ")" + " updates ID to " + this.getID() + " and sends a message " + newMinMaxPlusMessage + ", now MS is " + messageComplexity);
            } else {
                this.status = NodeStatus.DEFEATED;
                setColor(Color.BLACK);
                System.out.println("Node " + this.getID() + " becomes DEFEATED after receiving " + minMaxPlusMessage);
            }
        }
    }
}
