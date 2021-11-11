

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * This conducts some basic tests on the Warehouse class
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
        // set up the arr
        warehouses = new Warehouse[10];
        for(int i = 0; i < 10; i++){
            double x = Math.random() * 1000;
            double y = Math.random() * 1000;
            Point p = new Point(x,y);
            // i%3+1 ranges from 1-3, with i
            warehouses[i] = new Warehouse(p, i%3+1);
        }
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
        // Build a little manifest
        final DeQueue<ShipmentOrder> manifest = new DeQueue<ShipmentOrder>();
        manifest.add(new ShipmentOrder(warehouses[0],warehouses[1]));
        
        
        // We create an local-scoped subclass of truck, that just helps monitor some things
        class TestingTruck extends Truck{
            TestingTruck(){
                // variable capture: get the enclosing manifest variable
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
        
        TestingTruck test = new TestingTruck();
        // make sure the test truck isn't already done
        assertFalse(test.isComplete());
        
        // send the truck over
        test.action();
        // it should be in the first warehouse's queue
        assertEquals("1 Trucks entering, 0 Trucks leaving", warehouses[0].status());
        // now, it should be being processed
        warehouses[0].action();
        assertEquals("0 Trucks entering, 1 Trucks leaving", warehouses[0].status());
        // still paused though.
        assertTrue(test.status().indexOf("Paused")!=-1);
        test.action();

    }
}
