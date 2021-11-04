
/**
 * This is a single shipment: IE, a single combination
 * of a starting and destination wharehouse.  A class is
 * needed for this because we want a unique description for
 * the tuple of values that contains (at minimum) a pickup and a
 * destination warehouse.  A tuple is needed because we can't just
 * have a list of destinations and a list of pickups: a pickup needs
 * to be brought to a specific destination, not a random one!
 *
 * @author Calum McConnell
 * @version 0.0.1
 */
public class ShipmentOrder
{
    private ShipmentState state;
    
    // Final members may be made public without breaking encapsulation if:
    //      A) they are immutable
    //      B) the object referred to is shared anywas
    // B) is true here: so we just make them public, to make the API simpler
    public final Warehouse pickup;
    public final Warehouse destination;
    
    public ShipmentOrder(Warehouse start, Warehouse end){
        pickup = start;
        destination = end;
        state= ShipmentState.AWAITING_PICKUP;
    }
    
    /**
     * Gets the current status of the cargo being shipped
     */
    public ShipmentState getStatus(){
        return state;
    }
    
    /**
     * Gets the distance between the next target and the given point.
     * If the order has not been picked up, this target is the pickup spot:
     * otherwise, its the destination
     */
    public double getDistanceToTarget(Point p){
        Point other;
        if(state == ShipmentState.AWAITING_PICKUP){
            other = pickup.location;
        }
        else{
            other = destination.location;
        }
        return other.calculateDistance(p);
    }
    
    /**
     * Moves the current shipment state to the next stage
     * 
     * @throws IllegalStateException if the shipment is complete or invalid
     */
    public void nextState(){
        switch(state){
            case AWAITING_PICKUP:
                state = ShipmentState.MOVING;
                return;
            case MOVING:
                state = ShipmentState.DROPPED_OFF;
                return;
            case DROPPED_OFF:
                System.err.println("Error: shipment already completed.  No next state");
                // fall-through
            default:
                throw new IllegalStateException("Shipment state already complete or invalid");
        }
    }
}

 