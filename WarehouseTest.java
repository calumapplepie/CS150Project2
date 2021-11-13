import java.util.function.UnaryOperator;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * This conducts some tests on the Warehouse class
 *
 * @author  Calum McConnell
 * @version 0.0.1
 */
public class WarehouseTest
{
    
    Warehouse[] warehouses;
    /**
     * Sets up the test fixture, initializing the warehouses.
     *
     * Called before every test case method.
     */
    @BeforeEach
    public void setUp()
    {
        // set up the array of warehouses
        // we have two warehouses of each dock count: this lets us prepare all possible combinations
        // of manifests for destinaton and origin dock count
        warehouses = new Warehouse[6];
        for(int i = 0; i < 6; i++){
            // using Math.random() in a test suite is generally a bad idea: however,
            // the location is hopefully irrelevant, as long as it isnt the same.
            double x = Math.random() * 1000;
            double y = Math.random() * 1000;
            Point p = new Point(x,y);
            // i%3+1 ranges from 1-3, wrt i
            warehouses[i] = new Warehouse(p, i%3+1);
        }
        
    }
    
    public void floodWarehouseQueue(Warehouse target1,Warehouse target2){
        // each truck will be moving from target1 to target2, but they all need distinct manifests
        // of distinct (though equivalent) shipping orders.
        DeQueue<Truck> trucks = new DeQueue<Truck>();
        for(int i = 0; i<30; i++){
            var manifest = new DeQueue<ShipmentOrder>();
            // add two orders, so that we can check that it doesnt end too soon
            manifest.add(new ShipmentOrder(target1,target2));
            manifest.add(new ShipmentOrder(target1,target2));
            Truck newBoy = new TestingTruck(manifest);
            trucks.add(newBoy);
        }
        // we use this technique again, rather than having a dozen for-each loops
        UnaryOperator<Truck> func = (Truck t) -> {t.action(); return null;};
    }
    
    @Test
    /**
     * This asserts that the set up created each of the warehouses, and that they are all
     * set up with an empty queue
     */
    public void assertSetUpWorked(){
        for(Warehouse i : warehouses){
            assertEmptyQueue(i);
        }
    }
    
    /**
     * This ensures that a given warehouse has an empty queue.
     */
    public void assertEmptyQueue(Warehouse war){
        assertEquals("0 Trucks entering, 0 Trucks leaving", war.status());
    }

    @Test
    /**
     * This ensures that an example situation with a single truck works right,
     * with the truck correctly passing through various stages.
     */
    public void assertSingleTruckProcess(){
        // Build a little, basic manifest
        DeQueue<ShipmentOrder> manifest = new DeQueue<ShipmentOrder>();
        manifest.add(new ShipmentOrder(warehouses[0],warehouses[1]));
        
        TestingTruck test = new TestingTruck(manifest);
        // make sure the test truck isn't already done
        assertFalse(test.isComplete());
        
        // send the truck over
        test.action();
        // it should be in the first warehouse's queue
        assertEquals("1 Trucks entering, 0 Trucks leaving", warehouses[0].status());
        // now, it should be being processed
        warehouses[0].action();
        assertEquals("0 Trucks entering, 1 Trucks leaving", warehouses[0].status());
        // still paused though. discard the status containing the update, and make sure it doesnt change
        test.status();
        String status = test.status();
        assertTrue(status.indexOf("Paused")!=-1);
        test.action();
        // better not have done anything
        assertEquals(status,test.status());
        assertFalse(test.loaded);
        // now, let it go!
        warehouses[0].action();
        // freedom?
        status = test.status();
        assertTrue(status.indexOf("Paused") == -1);
        assertTrue(test.loaded);
        assertFalse(test.isComplete());
        // next.
        test.loaded = false;
        assertEmptyQueue(warehouses[0]);
        assertEmptyQueue(warehouses[1]);
        test.action();
        assertEquals("1 Trucks entering, 0 Trucks leaving",warehouses[1].status());
        // now, it should be being processed
        warehouses[1].action();
        assertEquals("0 Trucks entering, 1 Trucks leaving", warehouses[1].status());
        // still paused though. discard again
        test.status();
        status = test.status();
        assertTrue(status.indexOf("Paused")!=-1);
        test.action();
        // better not have done anything
        assertEquals(status,test.status());
        assertFalse(test.loaded);
        // better not have done anything
        assertEquals(status,test.status());
        assertFalse(test.loaded);
        // now, let it go!
        warehouses[1].action();
        // freedom?
        status = test.status();
        assertTrue(status.indexOf("Paused") != -1);
        assertTrue(test.isComplete());
        assertEmptyQueue(warehouses[1]);

    }
    
    /**
     * This subclass just exists to let us monitor some things
     * about the trucks we are testing with.  Since the truck doesn't matter
     * in these tests, we use a super-fast truck to make testing easier.
     */
    private class TestingTruck extends Truck{
        TestingTruck(DeQueue<ShipmentOrder> manifest){
            super(1,manifest,BadRouter.class,new Point(0,0));
        }
        
        public boolean loaded = false;
        public double getMoveSpeed(){
            return 99999;
        }
        public void loadingComplete(){
            loaded = true;
            super.loadingComplete();
        }
    };
}
