
/**
 * Selects each order in the manifest, one after the other.
 *
 * @author Calum McConnell
 * @version 0.0.1
 */
public class BadRouter extends Router
{
    DeQueue<ShipmentOrder> manifest;
    ShipmentOrder[] currentCargo;
    public BadRouter(DeQueue<ShipmentOrder> orders, ShipmentOrder[] hold){
        // clone manifest, to allow us to use the queue's popping methods
        manifest = orders.clone();
        currentCargo = hold;
    }
    
    public ShipmentOrder getNextOrder(Point cur){
        // even bothering to track this is very silly: in fact, it probably
        // adds more time then it will register, if it even registers any.
        // but, it's an important point for our examination.
        long startTime = System.nanoTime();
        // point is ignored: simply returns the next order
        if(currentCargo[0] != null){
            return currentCargo[0];
        }
        ShipmentOrder retval = manifest.popFront();
        increaseExecutionTime(System.nanoTime() - startTime);
        return retval;
    }
}
