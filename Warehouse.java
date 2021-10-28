
/**
 * A single warehouse, which "contains" goods to be shipped.
 *
 * @author Calum McConnell
 * @version 0.0.1
 */
public class Warehouse
{
    private final Point location;
    // factorio is a very fun game: I take their terminology`
    private final DeQueue<Truck> stacker = new DeQueue<Truck>();
    
    /**
     * Builds a warehouse
     * @param p the location to build the warehouse at
     * @param docks the number of loading docks that this warehouse has
     */
    public Warehouse(Point p, int docks){
        location = p;
    }
    
    /**
     * Adds the given truck to the queue
     */
    public void joinQueue(Truck t){
        
    }
}
