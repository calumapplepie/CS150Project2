

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * The test class SmallTruckTest. Extends TruckTest, which does most of the actual
 * work
 *
 * @author  Calum McConnell
 * @version 0.0.1
 */
public class SmallTruckTest extends TruckTest
{
    public SmallTruck generateTruckAtPoint(Point p){
        SmallTruck t = new SmallTruck(generateManifest(3),BadRouter.class,p);
        assertEquals(p,t.getLocation());
        return t;
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
