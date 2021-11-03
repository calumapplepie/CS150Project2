
/**
 * A midsize truck
 *
 * @author Calum McConnell
 * @version 0.0.1
 */
public class MediumTruck extends Truck
{
    MediumTruck(DeQueue<ShipmentOrder> cargo, Class<? extends Router> router, Point startingPoint){
        super(2, cargo, router, startingPoint);
    }
    
    public double getMoveSpeed(){
        return 6;
    }
}
