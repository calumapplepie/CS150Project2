
/**
 * This is a single shipment: IE, a single combination
 * of a starting and destination wharehouse.
 *
 * @author Calum McConnell
 * @version 0.0.1
 */
public class ShipmentOrder
{
    private ShipmentState state;
    private Warehouse destination;
    private Warehouse pickup;
}

enum ShipmentState{
    AWAITING_PICKUP, MOVING, DROPPED_OFF;
}
