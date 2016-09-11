public class SensorMeasurement {
    private long value;
    private long timestamp;

    public long getValue() {
        return this.value;
    }

    void setValue(final long value) {
        this.value = value;
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    void setTimestampToNow() {
        this.timestamp = System.currentTimeMillis();
    }

    void setTimestamp(final long timestamp) {
        this.timestamp = timestamp;
    }
}
