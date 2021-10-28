
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
     * This class gets the next warehouse that is to be moved to.
     * It uses an arbitrary algorithm to determine this: perhaps it routes to
     * the next order, or perhaps it finds the closest one, or perhaps it
     * solves the Travelling Salesman problem in O(N) time.  The only thing
     * it is certain NOT to do is route you somewhere completly useless:
     * thus, the routes do terminatte (eventually).
     * <p>
     * The shipment order returned might represent a pickup, or a drop-off:
     * check the status stored within it to be sure.
     * 
     * @return The next order to fulfill: it may be one that is already in-
     */
    public abstract ShipmentOrder getNextOrder();
    
}
