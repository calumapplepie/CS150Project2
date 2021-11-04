
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
     * @param dist the distance to move towards that point.
     * @return a new point, "dist" units closer to (or else simply equal to)
     * the other point.
     */
    public Point calculateNext(Point other, double dist)
    {
        //TODO
        return new Point(0,0);
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
