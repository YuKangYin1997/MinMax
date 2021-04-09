package ca.uottawa.csi5318.message;

public class MinMaxMessage {
    private String type;
    private int value;
    private int stage;

    public MinMaxMessage(String type, int value, int stage) {
        this.type = type;
        this.value = value;
        this.stage = stage;
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

    @Override
    public String toString() {
        return "{" +
                "type='" + type + '\'' +
                ", value=" + value +
                ", stage=" + stage +
                '}';
    }
}
