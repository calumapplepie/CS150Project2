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
    // defer whole-manifest traversals when possible
    private boolean complete = false;
    private final StringBuilder statusString;
    private int ticksOfFullCargo = 0;
    private int cargoCount =0;
    
    
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
            
            // we use a StringBuilder to build our strings.  that's becuase the building
            // and use of these strings is on the preformance critical path: it is 3
            // orders of magnitude faster than standard string concat methods. 
            statusString = new StringBuilder();
            
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
            
            // lastly, lets init the order
            currentOrder = router.getNextOrder(currentLocation);
    }
    
    /**
     * Moves the truck along: if it has arrived at a warehouse, join the
     * loading queue and prepare to empty cargo.
     */
    public void action(){
        if(paused == true){
            return;
        }      
        
        // What is our destiny?
        Warehouse destination = currentOrder.getTargetWarehouse();
        
        // move towards target
        currentLocation = currentLocation.calculateNext(destination.location, this.getMoveSpeed());
        
        // add to the cargo accumulator
        ticksOfFullCargo += cargoCount;
        
        // if we've arrived
        if(currentLocation.equals(destination.location)){
            // stop moving
            paused = true;
            // join the warehouse's queue
            destination.joinQueue(this);
            // note it in the log
            statusString.append("Joined entry queue for the warehouse at ");
            statusString.append(destination.location.toString());
            statusString.append(";  ");
        }
        
    }
    
    /**
     * Moves the current order on to the next stage; intended only
     * for use by Warehouses
     */
    protected void loadingComplete(){
        if(paused == false){
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
            // add this to the string log
            statusString.append("Picked up ");
            // we now have another piece of cargo!
            cargoCount++;
        }
        else{
            // we need to pull the order out of the array, it's completed
            for(int i = 0; i < currentCargo.length; i++){
                if(currentOrder == currentCargo[i]){
                    currentCargo[i] = null;
                    // if we are dropping off two, don't fail!
                    
                }
            }
            statusString.append("Dropped off ");
            // we lost a piece of cargo :(
            cargoCount --;
        }
        statusString.append("cargo at ");
        statusString.append(currentLocation.toString());
        statusString.append(";  ");
        
        // route now to avoid a conditional in an inner loop
        currentOrder = router.getNextOrder(currentLocation);
        
        // if it's null, then routing 'failed': there is no next order
        // that means we're done! we can go home!
        if(currentOrder == null){
            paused = true;
            complete = true;
            return;
        }    
    }
    
    /**
     * Prepares a string describing the current location, destination, and
     * the cargo.  This string is added to another string that notes any
     * changes in the past cycle: eg, cargo added or removed.
     * Caller is responsible for assuring that this is not called on a
     * truck that has completed it's orders: the results are nonsensical in that case
     */
    public String status(){
        // For the sake of performance, I use a StringBuilder
        // this is an API class that allows us to avoid repeatedly allocating parts of a string
        // it is semantically equivalent with a simple string +=, but this avoids memory allocations
        // it'd be nice if the compiler could spot that as well, buuuuutt it can't.
               
        // if we're done, don't bother with a status
        if(complete){
            return "";
        }
        
        // First, our location.
        statusString.append("Location: ");
        statusString.append(currentLocation.toString());
        
        // Now, our destination
        statusString.append(" Destination: ");
        if(currentOrder == null){
            statusString.append("None");
        }
        else{
            statusString.append(currentOrder.getTargetWarehouse().location.toString());
        }
        
        // and our cargo, displayed as filled/total
        statusString.append(" Cargo: ");
        
        statusString.append(cargoCount);
        statusString.append("/");
        statusString.append(currentCargo.length);
        
        // if we're paused, say so
        if(paused){
            statusString.append(" Paused, ");
            // we might be done!
            if(complete){
                statusString.append("all orders complete!");
            }
            else{
                statusString.append("awaiting warehouse");
            }
            
        }
        
        // convert to a proper string
        String status = statusString.toString();
        // and clear out the old from our builder
        statusString.setLength(0);
        return status;
    }
    
    /**
     * Whether or not we are finished.
     */
    public boolean isComplete(){
        return complete;
    }
    
    /**
     * Gets the move speed of the truck.
     * Subclasses override to get their correct speed
     */
    public abstract double getMoveSpeed();
    
    /**
     * Gets the trucks current location.  FOR SCIENCE.
     */
    public Point getLocation(){
        return currentLocation;
    }
    
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
    
    /**
     * Returns the ammount of time the router spent executing code,
     * in nanoseconds
     */
    public long routingTime(){
        return router.getExecutionTime();
    }
    
    /**
     * Gets the number of ticks spent with each piece of cargo
     * (eg, if you divide by the number of overall ticks, you get the average
     * ammount of cargo in the hold).
     */
    public int cargoFilledTime(){
        return ticksOfFullCargo;
    }
}
