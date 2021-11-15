
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
        ShipmentOrder retval;
        if(currentCargo[0] != null){
            retval = currentCargo[0];
        }
        // we should return null when we're done
        else if(manifest.size() == 0){
            retval = null;
        }
        else{
            retval = manifest.popFront();
        }
        increaseExecutionTime(System.nanoTime() - startTime);
        return retval;
    }
}
