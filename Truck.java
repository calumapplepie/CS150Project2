
/**
 * This is a truck.  Most functionality is provided in this class,
 * but some specifics (such as move speed) are given by the child 
 * classes.
 *
 * @author Calum McConnell
 * @version 0.1
 */
public class Truck
{
    private final ShipmentOrder[] currentCargo;
    private final DeQueue<ShipmentOrder> manifest;
    private final Router router;
    private Point currentLocation;
    
    /**
     * Creates a truck 
     */
    public Truck(
        ShipmentOrder[] cargoArray, 
        DeQueue<ShipmentOrder> cargoManifest,
        Router router,
        Point startingPoint){
            currentCargo = cargoArray;
            manifest = cargoManifest;
            this.router = router;
            currentLocation = startingPoint;
    }
    
}
