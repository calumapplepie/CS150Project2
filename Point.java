
/**
 * This represents a point in space, and provides some
 * methods to work with that point.
 * 
 * Points are immutable: this is simply for the fun of working
 * with immutable objects, which (for some languages) is basically
 * all objects.
 *
 * @author Calum McConnell
 * @version 0.0.1
 */
public class Point
{
    private final double xPos;
    private final double yPos;

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
}
