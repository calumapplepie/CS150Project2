
/**
 * Selects each order in the manifest, one after the other.
 *
 * @author Calum McConnell
 * @version 0.0.1
 */
public class NonsenseRouter extends Router
{
    DeQueue<ShipmentOrder> manifest;
    ShipmentOrder[] currentCargo;
    public NonsenseRouter(DeQueue<ShipmentOrder> orders, ShipmentOrder[] hold){
        // clone manifest, to allow us to use the queue's popping methods
        manifest = orders.clone();
        currentCargo = hold;
    }
    
    public ShipmentOrder getNextOrder(Point cur){
        // point is ignored: simply returns the next order
        if(currentCargo[0] != null){
            return currentCargo[0];
        }
        return manifest.popFront();
    }
}
