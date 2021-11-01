
/**
 * Selects a random destination, seeded off the position of the first
 * point in the manifest
 *
 * @author Calum McConnell
 * @version 0.0.1
 */
public class NonsenseRouter extends Router
{
    DeQueue<ShipmentOrder> manifest;
    public NonsenseRouter(DeQueue<ShipmentOrder> carg){
        manifest = carg;
    }
    
    public ShipmentOrder getNextOrder(){
        // TODO
        // return a random, but valid, order to fulfill
        return null;
    }
}
