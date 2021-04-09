package ca.uottawa.csi5318.message;

import ca.uottawa.csi5318.util.FibonacciSequence;

public class MinMaxPlusMessage {

    private String type;
    private int value;
    private int stage;
    private int distance;  // only has effect in an even stage

    public MinMaxPlusMessage(String type, int value, int stage, int distance) {
        this.type = type;
        this.value = value;
        this.stage = stage;
        this.distance = distance;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getStage() {
        return stage;
    }

    public void setStage(int stage) {
        this.stage = stage;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    @Override
    public String toString() {
        return "{" +
                "type='" + type + '\'' +
                ", value=" + value +
                ", stage=" + stage +
                ", distance=" + distance +
                '}';
    }
}
