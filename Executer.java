
/**
 * Executes a single run of a given state: constructing
 * the requested objects, and then
 *
 * @author Calum McConnell
 * @version 0.0.1
 */
public class Executer
{
    private DoublyLinkedList<Truck> trucks;
    private DoublyLinkedList<Warehouse> warehouses;
    
    
    /**
     * Called in a loop by start(), this method will run
     * through and schedule entities, increment the clock, and
     * more. 
     * 
     * @return false once it should be stopped
     */
    public boolean execute(){
        //todo
        return false;
    }
    
    /**
     * This method will handle the work of starting the simulation.
     * It will call preprare(), unless prepare() has already been called
     * 
     * This will run execute() repeatedly, until all trucks
     * are finished.  It (may) slow things down a bit, for your
     * viewing pleasure
     */
    public void start(){
        
    }
    
    /**
     * This does the groundwork: generating all the objects needed
     */
    public void prepare(){
        
    }
}
