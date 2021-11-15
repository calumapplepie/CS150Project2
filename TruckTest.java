import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Random;

/**
 * This provides some tests for the general Truck class.
 * Subclasses of truck should have test classes that extend
 * this one, which contain at least one @Test.
 * 
 * Trucks in this class are not given usable manifests
 * 
 * Lastly, while this class uses random numbers to generate points,
 * the numbers are deterministically generated, keeping the test stable
 *
 * @author Calum McConnell
 * @version 0.0.1
 */
public abstract class TruckTest
{
    Truck[] trucks;
    Warehouse[] warehouses;
    // because test order is deterministic, if unpredictable, we don't need to make this dynamic:
    // each test gets different points, but between repeated runs those points are the same
    Random rand = new Random(999);
    
    public Point generatePoint(){
        return new Point(rand.nextDouble()*100,rand.nextDouble()*100);
        
    }
    
    public DeQueue<ShipmentOrder> generateManifest(int length){
        DeQueue<ShipmentOrder> ships = new DeQueue<ShipmentOrder>();
        for(int i = 0; i < length; i++){
            ships.add(generateOrder());
        }
        return ships;
    }
    
    public ShipmentOrder generateOrder(){
        Warehouse start;
        Warehouse end;
        do{
            start = warehouses[rand.nextInt(10)];
            end = warehouses[rand.nextInt(10)];
        }while (start == end);
        return new ShipmentOrder(start,end);
    }
    // for testing specific 'weird cases'
    public abstract Truck generateTruckAtPoint(Point p);
    
    @BeforeEach
    public void setUp(){
        // set up warehouses first
        warehouses = new Warehouse[10];
        for(int i = 0; i< 10; i++){
            warehouses[i] = new Warehouse(generatePoint(), i%3+1);
        }
        
        trucks = new Truck[12];
        for(int i = 0; i < 10; i++){
            trucks[i]=generateTruckAtPoint(generatePoint());
        }
        trucks[10] = generateTruckAtPoint(new Point(0,0));
        trucks[11] = generateTruckAtPoint(new Point(-10,-10));
        
        
    }
    
    
    
    @Test
    public void trucksAreInitialized(){
        
    }
}
