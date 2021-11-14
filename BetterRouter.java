/**
 * This is a 'better' router: it will go to the closest order in the
 * manifest or cargo hold that it can, unless it's cargo is full:
 * in which case it goes to the closest order in the hold
 *
 * @author Calum McConnell
 * @version 0.0.1
 */
public class BetterRouter extends Router
{
    private DeQueue<ShipmentOrder> manifest;
    private ShipmentOrder[] currentCargo;
    public BetterRouter(DeQueue<ShipmentOrder> orders, ShipmentOrder[] hold){
        // clone manifest, to allow us to use the queue's popping methods
        manifest = orders.clone();
        // don't clone this, so we get a live reference
        currentCargo = hold;
    }
    
    public ShipmentOrder getNextOrder(Point currentLocation){
        long startTime = System.nanoTime();
        var closestCandidates = new DeQueue<ShipmentOrder>();
        
        // determine if we have a full cargo hold
        boolean fullCargo = true;
        for(ShipmentOrder i : currentCargo){
            if(i == null){
                fullCargo = false;
                break;
            }
        }
        
        // if the currentCargo is full, select from it
        if(fullCargo){
            for(ShipmentOrder i : currentCargo){
                closestCandidates.add(i);
            }
        }
        else{
            closestCandidates = manifest;
        }
        ShipmentOrder retval = getClosestOrder(closestCandidates, currentLocation);
        increaseExecutionTime(System.nanoTime()-startTime);
        return retval;
    }
    
    /**
     * Gets the order closest to the given point, considering whatever
     * part of the order must be accomplished next.
     * Will never return a completed order.  Returns null if
     * all orders are completed.
     */
    public ShipmentOrder getClosestOrder(DeQueue<ShipmentOrder> candidates, Point currentSpot){
        DoublyLinkedList<Double> distances = candidates.applyFunctionToList(
            (ShipmentOrder s) -> { 
                if(s.getStatus() == ShipmentState.DROPPED_OFF){
                    return -1.0;
                }
                return s.getTargetWarehouse().location.calculateDistance(currentSpot);
            });
        
        // iterate through: we can't just start from the first and find the least,
        // since there might not be any valid answer
        double leastDistance = Double.POSITIVE_INFINITY;
        ShipmentOrder closest = null;
        // reset the fake pointers before iteration
        candidates.resetFakeQueue();
        for(int i = 0; i < candidates.size(); i++){
            double curDist = distances.fakePop();
            // make sure this is a valid value, and is smaller
            if(curDist < leastDistance && curDist != -1){
                closest = candidates.fakePop();
                leastDistance = curDist;
            }
            else{
                // advance the pointer
                candidates.fakePop();
            }
        }
        return closest;
    }
}
