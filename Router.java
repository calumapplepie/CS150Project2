
/**
 * This class describes the API a truck will use for it's routing.
 * Each truck owns one router: the router informs the truck where
 * it's going, and has a copy of the manifest to make that decision
 *
 * @author Calum McConnell
 * @version 0.0.1
 */
public abstract class Router
{
    /**
     * The number of nanoseconds spent executing getNextOrder().
     * Obviously, we might also track the number of invocations of
     * getNextOrder: however, that can just be calculated from the
     * 
     */
    private long executionTime;
    
    /**
     * This class gets the next warehouse that is to be moved to.
     * It uses an arbitrary algorithm to determine this: perhaps it routes to
     * the next order, or perhaps it finds the closest one, or perhaps it
     * solves the Travelling Salesman problem in O(log N) time.  The only thing
     * it is certain NOT to do is route you somewhere truly useless:
     * thus, the routes do terminatte (eventually).
     * <p>
     * The shipment order returned might represent a pickup, or a drop-off:
     * check the status stored within it to be sure.
     * 
     * @return The next order to fulfill: it may be one that is already in-
     * progress, or it may be a new one.  Returns null when no orders remain.
     */
    public abstract ShipmentOrder getNextOrder(Point currentLocation);
    
    /**
     * This method is used by subclasses to increase the amount of time
     * the router has been executing.  It is to be called at the end of
     * getNextOrder() invocation
     * @param delta How many nanoseconds were required for the execution
     */
    protected void increaseExecutionTime(long delta){
        executionTime += delta;
    }
    
    /**
     * Returns the amount of time that this router object has spent in
     * getNextOrder() calls.
     */
    public long getExecutuionTime(){
        return executionTime;
    }
}
