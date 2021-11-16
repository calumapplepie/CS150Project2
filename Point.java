import java.util.Formatter;
import java.text.DecimalFormat;

/**
 * This represents a point in space, and provides some
 * methods to work with that point.
 * <p>
 * Points are immutable: this is simply for the fun of working
 * with immutable objects, which (for some languages) is basically
 * all objects.
 *
 * @author Calum McConnell
 * @version 0.0.1
 */
public class Point
{
    // because these are primitives and final, they are immutable:
    // allowing direct access to them is not a problem.
    public final double xPos;
    public final double yPos;
    // we use this to format our numbers, since it's much faster than Formatter
    // we allow for a lot of leading digits: some of our test cases are nasty
    public static final DecimalFormat formatter = new DecimalFormat("##################0.00");
    
    // okay, so not EVERYTHING is immutable: but this cache should speed things up a lot for some of our 'big spenders'
    private String cached = null;

    /**
     * Constructor for objects of class Point
     */
    public Point(double x, double y)
    {
        xPos = x;
        yPos = y;
    }

    /**
     * Calculates the next point on the line between
     * this point and the provided point, which is the
     * given distance away from this point on that line
     * <p>
     * If this would go past the other point, return the other point
     * instead
     * 
     * @param other The other point, which we are "heading towards"
     * @param distToMove the distance to move towards that point.
     * @return a new point, "dist" units closer to (or else simply equal to)
     * the other point.
     */
    public Point calculateNext(Point that, double distToMove)
    {
        // Time for some vector math!
        // Calculate a vector in the directon of 'that' of length dist
        // Done by converting the (this->that) vector into a unit vector,
        // then multiplying that unit vector by the distance to move.
        
        // in other words, take the delta for each axis, divide that by
        // the total length of the vector, then multiply by the amount
        // the new vector should move
        double totalDist = calculateDistance(that);
        double xDelta = (that.xPos - this.xPos)/totalDist * distToMove;
        double yDelta = (that.yPos - this.yPos)/totalDist * distToMove;
        
        // if we'd go too far, just return the destination point.
        if(totalDist < distToMove){
            return that;
        }
        
        // if it's asking us to stay still, don't return NaN's!
        if(distToMove == 0.0){
            return this;
        }
        
        return new Point(xPos+xDelta,yPos+yDelta);
    }
    
    /**
     * Produces a string representation of this point.
     * <p>
     * I recently took a break, deciding to play with preformance profiling.  It turns
     * out this one method consumes 6% of our overall CPU time, and that's without even
     * fully implementing all of its uses! The Formatter class is INCREADIBLY inefficent
     * in java.  So, I do some tomfoolery to get this very simple format to be assembled.
     */
    public String toString(){
        if(cached== null){
            cached = "(" + doubleToString(xPos) +", "+ doubleToString(yPos) + ")";
        }
        return cached;        
    }
    
    /**
     * This method exists to make it possible for the JIT to fully optimize the routines
     * that transform our points into strings.  Without this, we're stuck doing things
     * with formatters, which are surprisingly slow!
     * 
     * Is this uneeded and complex? Yes. But it solves a problem that I would otherwise suffer!
     */
    public String doubleToString(double d){
        long intPortion = (long) d;
        // get just the decimal
        d -= intPortion;
        // make sure it's positive, so we don't get random negatives floating around
        d = Math.abs(d);
        int decimalDigit1 = (int) (d *= 10);
        d-= decimalDigit1;
        int decimalDigit2 = (int) Math.round((d*=10));
        // check if we should have increased the int portion
        if(decimalDigit2 == 10){
            decimalDigit2 = 0;
            decimalDigit1++;
        }
        if(decimalDigit1 == 10){
            decimalDigit1 = 0;
            intPortion += Math.copySign(1,intPortion);
        }
        
        return intPortion + "." + decimalDigit1 + decimalDigit2;
    }
    
    /**
     * Determines the distance between two points on the grid.
     */
    public double calculateDistance(Point that){
        double xDelta2 = Math.pow(this.xPos - that.xPos,2);
        double yDelta2 = Math.pow(this.yPos - that.yPos,2);
        return Math.sqrt(xDelta2+yDelta2);
    }

    public boolean equals(Object o){
        // I HATE JAVA.  WHY DOES EQUIVALENCY NEED TO BE SO HARD?
        // WHY DOES EVERY PROGRAM NEED THIS TYPE OF BOILERPLATE, THAT
        // STILL RESULTS IN A TECHNICALLY-INCORRECT RESULT IN JAVA'S INHERITENCE?
        // WHY????
        if(! (o instanceof Point)){
            return false;
        }
        return equals((Point) o);
    }
    
    /**
     * Returns true if both point objects refer to the same coordinate.
     * Tolerates a small epsilon.
     * 
     * @return true if the points are equal to each other
     */
    public boolean equals(Point p){
        return floatingEquals(xPos, p.xPos) && floatingEquals(yPos,p.yPos);
    }
    
    /**
     * Verifies that the two doubles are equal to each other,
     * with a tolerance equal to 10 units of least precision.
     * <p>
     * This should REALLY just be in the java standard library:
     * toDegrees() exists, and that's much simpler, so why can't this?!?!?!
     */
    public static boolean floatingEquals(double a, double b){
        // we make sure that difference between the two is less than 10
        // Units of Least precision: ie, that it is within 10 numbers
        // of the right answer.
        // We also have an absolute tolerance, in case we are
        // comparing two near-zero values
        double delta = Math.abs(a-b);
        return delta < Math.ulp(a/2) * 10 || delta < 1e-9;
    }
}
