
/**
 * This is a truck.  Most functionality is provided in this class,
 * but some specifics (such as move speed) are given by the child 
 * classes.
 *
 * @author Calum McConnell
 * @version 0.1
 */
public abstract class Truck implements Scheduleable
{
    private final ShipmentOrder[] currentCargo;
    private final DeQueue<ShipmentOrder> manifest;
    private final Router router;
    private Point currentLocation;
    
    /**
     * Creates a truck.  Most parts of the truck are expected to be
     * produced seperately, just like how real trucks are made in
     * several contries then assembled in the US.  This is to allow
     * generators flexibility in what they create: the generator
     * class, not the truck, decides what the configuration is.
     * 
     * @param cargoArray an array representing the cargo: it should
     * be empty to start, and of length equal to how much cargo the truck
     * can hold.
     * 
     * @param cargoManifest a complete, finalized manifest of cargo orders,
     * which will be carried out by this truck
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
            
            // make extra sure nobody can screw with our orders
            manifest.lock();
    }
    
}
