
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
    
    // Final members may be public without breaking encapsulation if:
    //      A) they are immutable
    //      B) the reference is shared
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
     * Moves the current shipment state to the next stage
     */
    public void nextState(){
        //TODO
    }
}

 