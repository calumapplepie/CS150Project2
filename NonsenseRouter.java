
/**
 * Selects a random destination, seeded off the position of the first
 * point in the manifest
 *
 * @author Calum McConnell
 * @version 0.0.1
 */
public class NonsenseRouter
{
    DeQueue<ShipmentOrder> manifest;
    public NonsenseRouter(DeQueue<ShipmentOrder> carg){
        manifest = carg;
    }
}
