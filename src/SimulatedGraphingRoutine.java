public class SimulatedGraphingRoutine {
    public static void main(String args[]) {
        SimulatedGraphingRoutine simulation = new SimulatedGraphingRoutine();
        
        simulation.drawStuff();
    }
    
    public void drawStuff() {
        SimulatedSensor sensor1, sensor2;
        
        SensorMeasurementRingBuffer buffer1, buffer2;
        
        buffer1 = SensorMeasurementRingBuffer.createBuffer(5000);
        buffer2 = SensorMeasurementRingBuffer.createBuffer(5000);
        
        sensor1 = new SimulatedSensor(buffer1);
        sensor2 = new SimulatedSensor(buffer2);
        
        new Thread(sensor1).start();
        new Thread(sensor2).start();
        
        ImaginaryWindowBoundaries bounds = new ImaginaryWindowBoundaries();
        
        while(true) {
            // It is time to paint the screen
            imaginaryClearTheScreen();
            
            computeWindowBoundaries(bounds);
            
            drawLine(buffer1, "Red", bounds);
    
            drawLine(buffer2, "Blue", bounds);       
            
            try {
                // Refresh the screen 10 times a second
                Thread.sleep(100);
            } catch (InterruptedException e) {
                // do nothing
            }
        }
    }
    
    private void drawLine(SensorMeasurementRingBuffer b, String color, 
                          ImaginaryWindowBoundaries boundaries) {
        SensorMeasurementConsumer c = b.createConsumer();
        
        SensorMeasurement current, last = null;
        
        while(c.countAvailable() > 0) {
            current = c.get();
            if((current.getTimestamp() >= boundaries.minTimestamp) && 
               (current.getTimestamp() <= boundaries.maxTimestamp)) {
                if(last != null) {
                    // draw line from last.getValue() to current.getValue()
                    // no actual drawing here in the example code
                }
                
                last = c.getPrevious();
            }
        }
        
    }
    
    private void imaginaryClearTheScreen() {
        // If this were a real method it would remove all drawing from the screen
    }
    
    private void computeWindowBoundaries(ImaginaryWindowBoundaries b) {
        // If this were a real method it would consider the current
        // "zoom" and range settings chosen by the user and figure out
        // the minimum and maximum range of measurement timestamps we
        // will be displaying.
        
        // I am just going to hardcode silly values because this is an
        // imaginary method.
        
        b.minTimestamp = 12L;
        b.maxTimestamp = System.currentTimeMillis();
    }
}