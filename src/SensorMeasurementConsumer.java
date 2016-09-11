public interface SensorMeasurementConsumer {
    int countAvailable();
    SensorMeasurement get();
}
