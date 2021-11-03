
/**
 * A large truck
 *
 * @author Calum McConnell
 * @version 0.0.1
 */
public class LargeTruck extends Truck
{
    LargeTruck(DeQueue<ShipmentOrder> cargo, Class<? extends Router> router, Point startingPoint){
        super(3, cargo, router, startingPoint);
    }
    
    public double getMoveSpeed(){
        return 3;
    }
}
