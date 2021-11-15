

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * The test class SmallTruckTest. Extends TruckTest, which does most of the actual
 * work
 *
 * @author  (your name)
 * @version (a version number or a date)
 */
public class SmallTruckTest extends TruckTest
{
    public SmallTruck generateTruckAtPoint(Point p){
        return new SmallTruck(generateManifest(3),BadRouter.class,generatePoint());
    }
    
    @Test
    /**
     * The tests of this class are mostly defined in the parent, and they
     * will be inherited and run, but only if BlueJ recognises this as a test class.
     */
    public void thisIsARealTestClass(){
        assertEquals(0,0);
    }
}
