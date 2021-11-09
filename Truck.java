import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import javax.swing.JComponent;

import java.lang.reflect.Constructor;

/**
 * This is a truck.  Most functionality is provided in this class,
 * but some specifics (such as move speed) are given by the child 
 * classes.
 *
 * @author Calum McConnell
 * @version 0.1
 */
public abstract class Truck implements Schedule, Render
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
     * @param cargoSize the number of cargo items that this truck can hold
     * 
     * @param cargoManifest the manifest object that this truck will be working from
     * 
     * @param routerClass A Constructor object representing a constructor for a 
     * Router subclass, which wil be used to build our routers as we go.
     * 
     * 
     * @param cargoManifest a complete, finalized manifest of cargo orders,
     * which will be carried out by this truck
     */
    public Truck(
        int cargoSize,
        DeQueue<ShipmentOrder> cargoManifest,
        Class<? extends Router> routerClass,
        Point startingPoint){
            currentCargo = new ShipmentOrder[cargoSize];
            manifest = cargoManifest;
            currentLocation = startingPoint;
            
            // make extra sure nobody can screw with our orders
            manifest.lock();
            
            // Now, lets create our router!
            try{
                Constructor<? extends Router> routerFactory = 
                    routerClass.getConstructor(cargoManifest.getClass(),ShipmentOrder[].class);
                router = routerFactory.newInstance(cargoManifest,currentCargo);
                // I could have done this without reflection, by building the router seperately
                // and then passing it in, but every one of my projects needs at least one thing
                // that is totally overdone.
            }
            catch(Exception e){
                // I also could have just used lambdas, and passed those in.  However,
                // I haven't done much with reflection before, and I wanted to try it out
                System.err.println("Unexpected exception when building the router");
                e.printStackTrace();
                // we absolutely must terminate: otherwise, we failed to initialize
                // a final field
                throw new Error("Failed to create router object");
            }
            
            
    }
    
    /**
     * Moves the truck along: if it has arrived at a warehouse, join the
     * loading queue and prepare to empty cargo.
     */
    public void action(){
        // go to router if no order
        if(currentOrder == null){
            currentOrder = router.getNextOrder(currentLocation);
        }
        
        // move towards target
        currentLocation = currentLocation.calculateNext(currentOrder.getTargetWarehouse().location, this.getMoveSpeed());
        
    }
    
    /**
     * Moves the current order on to the next stage; intended only
     * for use by Warehouses
     */
    protected void loadingComplete(){
        if(paused != false){
            throw new Error("A truck is trying to leave a warehouse it never entered");
        }
        paused = false;
        currentOrder.nextState();
        if(currentOrder.getStatus() == ShipmentState.MOVING){
            // place the current order in the first empty slot in the array
            int i = 0;
            while(currentCargo[i] != null){
                // this will throw an exception if the array is full:
                // that is OK, since we shouldnt be in this method if it is
                i++;
            }
            currentCargo[i] = currentOrder;
        }
        
        // set current order to null to trigger re-routing on next cycle
        currentOrder = null;
    }
    
    /**
     * Prepares a string describing the current location, destination, and
     * the cargo.  This string is added to another string that notes any
     * changes in the past cycle: eg, cargo added or removed.
     */
    public String status(){
        // TODO
        return "";
    }
    
    public boolean isComplete(){
        // restart the fake queue
        manifest.resetFakeQueue();
        for(int i = 0; i < manifest.size(); i++){
            if(manifest.fakePop().getStatus() != ShipmentState.DROPPED_OFF){
                return false;
            }
        }
        return true;
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
    
    /**
     * This creates a shape appropriate to the truck described by this object, and then
     * paints it onto the given Graphics object.  The pictures of trucks are centered on
     * their locations, and are sized and colored according to their contents.
     * Filled cargo units are shown as orange squares, while empty ones are red.
     * Thus, the overall truck is a rectangle, with the cargo displayed inside
     */
    public void draw(Graphics2D g){
        // Don't draw a truck that is currently waiting in a warehouse
        if(paused){
            return;
        }
        // Lets calculate the various points from which we will draw our rectangles.
        // We are centered on the currentLocation: so we draw from a bit above it
        double verticalCoordinate = currentLocation.yPos - Configuration.objectSize/2;
        
        // The horisontal center is, once again, equal to the position less the length of the object
        // But we need to account for the length varying, with different sized trucks
        double length = Configuration.objectSize * currentCargo.length;
        double horisontalCoordinate = currentLocation.xPos - length / 2;
        
        // Now, we produce the rectangles that make up the truck, 
        // adding them to the graphics object
        for(int i = 0; i < currentCargo.length; i++){
            // build the rectangle.  We use a high-precision Rectangle2D.Double, mostly because I don't
            // want to write casts from our double-based points into their integer-based coradinates
            Rectangle2D rect = new Rectangle2D.Double(horisontalCoordinate,verticalCoordinate,
                Configuration.objectSize,Configuration.objectSize);
            // now set the color the next object will be, based on whether or not this unit is full
            if(currentCargo[i] == null){
                g.setColor(Configuration.emptyColor);
            }
            else{
                g.setColor(Configuration.filledColor);
            }
            
            // now draw!
            g.fill(rect);
            
            // increment coordinate for next loop
            horisontalCoordinate += Configuration.objectSize;
        }
    }
}
