public class BufferTest {
    public static void main(String[] args) {
        SensorMeasurementRingBuffer b = SensorMeasurementRingBuffer.createBuffer(4);

        SensorMeasurementConsumer c1 = b.createConsumer();
        SensorMeasurementConsumer c2 = b.createConsumer();

        b.publishNow(100);
        b.publishNow(200);

        showInColumns(1, c1.get().getValue()); //should show 100

        b.publishNow(300);

        showInColumns(2, c2.get().getValue()); // should show 100

        b.publishNow(400);
        b.publishNow(500);

        SensorMeasurementConsumer c3 = b.createConsumer();

        b.publishNow(600);

        showInColumns(2, c2.get().getValue()); // buffer has wrapped, 200 has been overwritten

        b.publishNow(700);

        showInColumns(2, c2.get().getValue());

        //All of these should show the remaining numbers that have not been consumed

        while(c1.countAvailable() > 0) {
            showInColumns(1, c1.get().getValue());
        }

        while(c2.countAvailable() > 0) {
            showInColumns(2, c2.get().getValue());
        }

        while(c3.countAvailable() > 0) {
            showInColumns(3, c3.get().getValue());
        }
    }

    private static void showInColumns(int columnOffset, long value) {
        for(int i = 0; i < columnOffset; i++) {
            System.out.print("     ");
        }
        System.out.println(value);
    }
}
