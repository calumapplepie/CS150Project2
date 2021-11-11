

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.RepeatedTest;

/**
 * This tests Point's for various properties and behaviors, using primarally handmade
 * testcases
 *
 * @author  (your name)
 * @version (a version number or a date)
 */
public class PointTest
{
    @Test
    /**
     * A silly test, intended to see whether java applies a certain
     * optimization (realizing that identical creations of immutable objects are identical)
     * 
     * It does not.
     */
    public void testImmutableOptimization(){
        for(int i = -200; i < 200; i++){
            for(int j = -200; j < 200; j++){
                immutableIdentityTester(i,j);
            }
        }
    }
    
    /**
     * This does the actual check for the testImmutableOptimization method.
     * It has been branched off to (hopefully) encourage the JVM to apply every
     * optimization it can, as well as to do some additional identity tests
     */
    public void immutableIdentityTester(int i,int j){
        // lets see if java can optimize this at compile time
        Point a = new Point(i,j);
        Point b = new Point(i,j);
        // hashCode() is, by default, the system memory address.
        // we use assertNotEquals to pretend that this lack of optimization is good.
        assertNotEquals(a.hashCode(), b.hashCode());
        
        // might as well check some other facts about identical points
        assertEquals(a,b);
        assertTrue(a.equals(b));
        assertTrue(b.equals(a));
        // we should have 0 distance: I don't care that rounding exists, it shouldn't occur here!
        assertEquals(a.calculateDistance(b),0);
        
        // one more thing: check that the distances work as they should with identity
        // that is: travelling 0 units creates a different point, and travelling to far
        // reproduces an existing point
        Point aStill = a.calculateNext(b,0);
        Point aMover = a.calculateNext(b,1);
        // these points should be equal in location
        assertEquals(b,aStill);
        assertEquals(a,aMover);
        // but the still has the identity of a
        assertEquals(a.hashCode(),aStill.hashCode());
        // and the mover has the identity of b
        assertEquals(b.hashCode(),aMover.hashCode());        
    }
}
