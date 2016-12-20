public class SimulatedSensor implements Runnable {
    
    private SensorMeasurementRingBuffer buffer;
    private java.util.Random r;
    
    public SimulatedSensor(SensorMeasurementRingBuffer buffer) {
        this.buffer = buffer;
        r = new java.util.Random();
    }
    
    public void run() {
        do {
            try {
                buffer.publishNow((long)r.nextInt(200));
                Thread.sleep(200);
                
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (true);
    }
}