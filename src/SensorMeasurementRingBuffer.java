import java.util.LinkedList;
import java.util.List;

/**
 * Designed for a single publisher with one or more consumers
 */
public class SensorMeasurementRingBuffer {

    final boolean DEBUG_OUTPUT = false;
    
    private Object lockObj;

    private SensorMeasurement[] buffer;

    private int publishRef = 0;
    private long virtualCount = 0;

    private List<SensorMeasurementBufferConsumer> consumers = new LinkedList<>();

    private SensorMeasurementRingBuffer() {
        lockObj = new Object();
    }

    public static SensorMeasurementRingBuffer createBuffer(final int size) {
        SensorMeasurementRingBuffer result = new SensorMeasurementRingBuffer();

        result.buffer = new SensorMeasurement[size];

        for(int offset = 0; offset < size; offset++) {
            result.buffer[offset] = new SensorMeasurement();
        }

        return result;
    }

    public void publish(final long value, final long timestamp) {
        // If there is too much contention on the lockObj we should just maintain a list of pending values to be published
        // once the lock is available.  If we assume a single publisher no synchronization of this pending list would be
        // needed.
        synchronized (lockObj) {
            this.buffer[publishRef].setValue(value);
            this.buffer[publishRef].setTimestamp(timestamp);

            int beforePublishRef = publishRef;

            if (++publishRef == this.buffer.length) {
                publishRef = 0;
            }
            virtualCount++;

            for (SensorMeasurementBufferConsumer consumer : consumers) {
                consumer.notifyPublish(beforePublishRef);
            }

            if (DEBUG_OUTPUT) {
                System.out.print("[");
                for (int i = 0; i < buffer.length; i++) {
                    if (i > 0) {
                        System.out.print(" ");
                    }
                    System.out.print(buffer[i].getValue());
                }
                System.out.println("]");
            }
        }
    }

    public void publishNow(final long value) {
        publish(value, System.currentTimeMillis());
    }

    public SensorMeasurementConsumer createConsumer() {
        synchronized (lockObj) {
            SensorMeasurementBufferConsumer c = new SensorMeasurementBufferConsumer();
            consumers.add(c);
            return c;
        }
    }

    public void removeConsumer(final SensorMeasurementConsumer consumer) {
        synchronized (lockObj) {
            consumers.remove(consumer);
        }
    }

    private class SensorMeasurementBufferConsumer implements SensorMeasurementConsumer {

        private int readPoint;
        private long virtualReadCount;
        
        private SensorMeasurement lastReturnedMeasurement;

        SensorMeasurementBufferConsumer() {
            lastReturnedMeasurement = new SensorMeasurement();
            
            synchronized (lockObj) {
                readPoint = publishRef;
                if(virtualCount > buffer.length){
                    virtualReadCount = publishRef;
                } else {
                    virtualReadCount = virtualCount;
                }
            }
        }

        public int countAvailable() {
            synchronized (lockObj) {
                return (int) (virtualCount - virtualReadCount);
            }
        }
        public SensorMeasurement get() {
            synchronized (lockObj) {
                if(countAvailable() > 0) {
                    SensorMeasurement returnValue = buffer[readPoint];
                    incrementReadPoint();
                    lastReturnedMeasurement.setValue(returnValue.getValue());
                    lastReturnedMeasurement.setTimestamp(returnValue.getTimestamp());
                    return returnValue;
                } else {
                    return null;
                }
            }
        }
        
        public SensorMeasurement getPrevious() {
            synchronized (lockObj) {
                return lastReturnedMeasurement;
            }
        }


        private void incrementReadPoint() {
            if(++readPoint >= buffer.length) {
                readPoint = 0;
            }
            virtualReadCount++;
        }

        private void notifyPublish(final int beforePublishRef) {
            synchronized (lockObj) {
                if((virtualCount > buffer.length) && (beforePublishRef == readPoint)){
                    incrementReadPoint();
                }
            }
        }
        
        public void reset() {
            synchronized (lockObj) {
                readPoint = publishRef;
                incrementReadPoint();
            }
        }
        
        public void flush() {
            synchronized (lockObj) {
                readPoint = publishRef -1;
                if(readPoint < 0) {
                    readPoint = buffer.length -1;
                }
            }
        }
    }
}
