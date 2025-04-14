
public class Event {
    private String permission;
    private String type;
    private String state;
    private double number;
    private long timestamp;
    private double score;
    private Float reconstructionError;
    private Float reconstructionThreshold;
    private long totalBackgroundUsage;
    private long totalForegroundUsage;

    public Event(String permission, String type, String state, double number, long timestamp, double score, Float reconstructionError, Float reconstructionThreshold) {
        this.permission = permission;
        this.type = type;
        this.state = state;
        this.number = number;
        this.timestamp = timestamp;
        this.score = score;
        this.reconstructionError = reconstructionError;
        this.reconstructionThreshold = reconstructionThreshold;
    }

    public String getPermission() {
        return this.permission;
    }

    public String getType() {
        return this.type;
    }

    public String getState() {
        return this.state;
    }

    public double getNumber() {
        return this.number;
    }

    public double getScore() {
        return this.score;
    }

    public Float getReconstructionError() {
        return this.reconstructionError;
    }

    public Float getReconstructionThreshold() {
        return this.reconstructionThreshold;
    }

    public long getTotalBackgroundUsage() {
        return totalBackgroundUsage;
    }

    public long getTotalForegroundUsage() {
        return totalForegroundUsage;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public void setReconstructionError(Float reconstructionError) {
        this.reconstructionError = reconstructionError;
    }

    public void setReconstructionThreshold(Float reconstructionThreshold) {
        this.reconstructionThreshold = reconstructionThreshold;
    }
}
