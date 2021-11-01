
/**
 * A single warehouse, which "contains" goods to be shipped.
 *
 * @author Calum McConnell
 * @version 0.0.1
 */
public class Warehouse implements Schedule
{
    public final Point location;
    // factorio is a very fun game: I take their terminology`
    private final DeQueue<Truck> enter = new DeQueue<Truck>();
    private final DeQueue<Truck> exits = new DeQueue<Truck>();
    
    /**
     * Builds a warehouse
     * @param p the location to build the warehouse at
     * @param docks the number of loading docks that this warehouse has
     */
    public Warehouse(Point p, int docks){
        location = p;
    }
    
    /**
     * Adds the given truck to the entrance queue
     */
    public void joinQueue(Truck t){
        
    }
    
    /**
     * Steps through: a number of trucks equal to the loading docks
     * are popped out of the queue, and given the appropriate cargo,
     * before being placed into an exit queue.  
     */
    public void action(){
        
    }
    
    /**
     * Prepares a log string indicating how many trucks are in the entry
     * and exit queues.
     */
    public String status(){
        return "";
    }
}
