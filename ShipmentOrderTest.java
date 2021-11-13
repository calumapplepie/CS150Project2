

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * This does some tests on ShipmenetOrders
 *
 * @author  (your name)
 * @version (a version number or a date)
 */
public class ShipmentOrderTest
{
    Warehouse [] warehouses;
    DoublyLinkedList<ShipmentOrder> orders;

    /**
     * Sets up the test fixture.
     *
     * Called before every test case method.
     */
    @BeforeEach
    public void setUp()
    {
        // set up the array of warehouses
        warehouses = new Warehouse[10];
        for(int i = 0; i < 10; i++){
            // using Math.random() in a test suite is generally a bad idea: however,
            // the location is hopefully irrelevant
            double x = Math.random() * 1000;
            double y = Math.random() * 1000;
            Point p = new Point(x,y);
            // i%3+1 ranges from 1-3, wrt i
            warehouses[i] = new Warehouse(p, i%3+1);
        }
        // set up the DoublyLinkedList of orders; this is one of the few times we
        // DON'T use a DeQueue, just to check out my base class's uses
        orders = new DoublyLinkedList<ShipmentOrder>();
        for(Warehouse i : warehouses){
            for(Warehouse j : warehouses){
                // no orders from one place back to that same place.
                if(i==j){
                    assertThrows(Error.class, ()->new ShipmentOrder(i,j));
                    continue;   
                }
                orders.add(new ShipmentOrder(i,j));
            }
        }
    }
    
    @Test
    /**
     * Tests every single combination of warehouses into shipping orders
     * to assert that the state-progressing methods of the ShipmentOrder
     * work as defined
     */
    public void testStateManagementLong(){
        // We wanna run the same function on every member of that list:
        // so lets construct a lambda.
        orders.applyFunctionToList( (ShipmentOrder t) -> {
                // there is no reason an assert can't be done in a lambda
                // (other than irrelevant sillikness like "moral decency")
                assertEquals(ShipmentState.AWAITING_PICKUP, t.getStatus());
                assertEquals(t.pickup,t.getTargetWarehouse());
                t.nextState();
                
                // okay, perhaps this is ugly and unecessary
                // but the world betrayed me, and now it's time for my revenge!
                // THE WORLD SHALL FEAR LAMBDA-MAN!
                assertEquals(ShipmentState.MOVING, t.getStatus());
                assertEquals(t.destination, t.getTargetWarehouse());
                t.nextState();
                
                // dang, now Superman showed up and tried to fight me
                // but my supervillan origin story will not be ended so easily!
                assertEquals(ShipmentState.DROPPED_OFF, t.getStatus());
                assertThrows(IllegalStateException.class, ()->t.getTargetWarehouse() );
                
                
                return null;
            }
        );
    }
}
