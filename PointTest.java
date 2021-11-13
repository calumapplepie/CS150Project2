

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
    public void testToStringForFloats(){
        // these numbers are really, REALLY big: too big for an int
        Point reallyBig = new Point(-9999999999l,99999999999l);
        Point ittyBitty = new Point(0.000000009,0.00000000009);
        Point boring    = new Point(2.01 ,-2.01 );
        Point lessBoring= new Point(2.011,-2.011);
        
        // the number is big, but hopefully not so big that it can't be represented in the
        // mantissa without rounding.
        assertEquals("(-9999999999.00, 99999999999.00)", reallyBig.toString());
        
        // a small point is rounded to 0 on display
        assertEquals("(0.00, 0.00)", ittyBitty.toString());
        // but is different in storage
        assertNotEquals(new Point(0,0), ittyBitty);
        
        // do the same, with some normal-ish points.
        assertEquals("(2.01, -2.01)",boring.toString());
        assertEquals(boring.toString(),lessBoring.toString());
        assertNotEquals(boring,lessBoring);
        
        // Now just check that all these weird cases get distances calculated correctly
        testPointMovementCircle(reallyBig,1_000_000);
        testPointMovementCircle(ittyBitty,0.000_000_1);
        testPointMovementCircle(boring,0.000001);
        testPointMovementCircle(lessBoring,10_000_000);
        
    }
    
    @Test
    /**
     * A silly test, intended to see whether java applies a certain
     * optimization (realizing that identical creations of immutable objects are identical)
     * <p>
     * It does not.
     * <p>
     * While this started as silly, since it calls a helper method on sooo
     * many values, I eventually built it into a major test, by making the
     * helper method do ever-more complicated stuff.
     */
    public void testImmutableOptimization(){
        // Is testing 160,000 points too much? maybe.
        // but hey, it's good to be really, REALLY sure.  and 5 seconds isnt THAT long
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
        
        // Test the toString here, because WE CAN
        String resultString = "("+i+".00, "+j+".00)";
        assertEquals(resultString, a.toString());
        
        // and the movement (outside of identities)
        testPointMovementCircle(a,1.1);
    }
    
    /**
     * This tests that moving a given point along any direction works correctly.
     * (ie, any direction in a large circle works)
     * 
     * @parm move given so that tests can work for numbers really
     * big and really small (where a motion of 1 will cause rounding)
     * @param p the point being tested
     */
    public void testPointMovementCircle(Point p, double move){
        // this whirls around the unit circle: theta is in radians
        for(double theta = 0; theta <= Math.PI*2; theta+= .1){
            // x comp of vector of length l in direction theta
            // is cos(theta) * length
            double xDelta = Math.cos(theta) * move;
            double yDelta = Math.sin(theta) * move;
            testPointDeltaMove(p, xDelta, yDelta);
        }
        
        // also check the cardinal directions
        // no easier way to do this.
        testPointDeltaMove(p, move,    0);
        testPointDeltaMove(p,-move,    0);
        testPointDeltaMove(p,    0, move);
        testPointDeltaMove(p,    0,-move);
    }
    
    private void testPointDeltaMove(Point p, double xDelta, double yDelta){
        double xExpect = p.xPos+xDelta;
        double yExpect = p.yPos+yDelta;
        // make sure it doesn't just give us the saturation (the 'don't go too far' logic)        
        Point direction = new Point(xExpect+xDelta,yExpect+yDelta);
        Point expected  = new Point(xExpect  ,yExpect );
        // yeah, we're doing a lot of rounding-inducing floating-point math.
        // the Point class is being tested on it's tolerance of those rounds.
        double distance = Math.sqrt(xDelta*xDelta + yDelta*yDelta);
        Point calculated= p.calculateNext(direction, distance);
        assertEquals(expected,calculated);
        
        // also check that the point isn't equal to the original: because that would mess things up
        // (make the test completly irrelevant);
        assertNotEquals(p,calculated);
    }
}
