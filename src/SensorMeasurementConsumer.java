public interface SensorMeasurementConsumer {
    int countAvailable();
    SensorMeasurement get();
    SensorMeasurement getPrevious();
    
    void reset(); // Move consumer back to the oldest entry in the ringbuffer
    
    void flush(); // Move consumer to the end of the ringbuffer so there is nothing
                  // to read until something new is published
}
