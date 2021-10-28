/**
 * This enum describes the states that a shipment can be in.
 * While the simulation would be able to function without this type of tracking,
 * we might as well just include it, since it simplifies some tests and
 * makes a progress bar easier.
 * <p>
 * It also enables some extra bug checks that ensure no shipment is moved twice,
 * and no shipment is marked as complete without first being moved.
 *
 * @author Calum McConnell
 * @version 1
 */
public enum ShipmentState{
    /**
     * The shipment is sitting at its source warehouse
     */
    AWAITING_PICKUP, 
    
    /**
     * The shipment is in a truck
     */
    MOVING, 
    
    /**
     * The shipment has been dropped off
     */
    DROPPED_OFF;
}
