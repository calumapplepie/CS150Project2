
/**
 * This is a truck.  Most functionality is provided in this class,
 * but some specifics (such as move speed) are given by the child 
 * classes.
 *
 * @author Calum McConnell
 * @version 0.1
 */
public abstract class Truck implements Schedule
{
    private final ShipmentOrder[] currentCargo;
    private final DeQueue<ShipmentOrder> manifest;
    private final Router router;
    private ShipmentOrder currentOrder;
    private Point currentLocation;
    private boolean paused = false;
    
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
        int cargoSize, 
        DeQueue<ShipmentOrder> cargoManifest,
        Router router,
        Point startingPoint){
            currentCargo = new ShipmentOrder[cargoSize];
            manifest = cargoManifest;
            this.router = router;
            currentLocation = startingPoint;
            
            // make extra sure nobody can screw with our orders
            manifest.lock();
    }
    
    /**
     * Moves the truck along: if it has arrived at a warehouse, join the
     * loading queue and prepare to empty cargo.
     */
    public void action(){
        
    }
    
    /**
     * Prepares a string describing the current location, destination, and
     * the cargo.  This string is added to another string that notes any
     * changes in the past cycle: eg, cargo added or removed.
     */
    public String status(){
        return "";
    }
    
    /**
     * Moves the truck towards the given destination.  If we wind up on it,
     * then rejoice, and join the queue
     */
    public void move(){
        Warehouse destination;
        switch(currentOrder.getStatus()){
            case AWAITING_PICKUP:
                destination = currentOrder.pickup;
                break;
            case MOVING:
                destination = currentOrder.destination;
            default:
                // throw an unchecked exception: stuff broke
                throw new Error("Invalid order status");
        }
        Point newLocation = currentLocation.calculateNext(destination.location,getMoveSpeed());
    }
    
    /**
     * Gets the move speed of the truck.
     * Subclasses override to get their correct speed
     */
    public abstract double getMoveSpeed();
}
