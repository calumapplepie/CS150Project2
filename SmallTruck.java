
/**
 * A small truck
 *
 * @author Calum McConnell
 * @version 0.0.1
 */
public class SmallTruck extends Truck
{
    SmallTruck(DeQueue<ShipmentOrder> cargo, Router router, Point startingPoint){
        super(1, cargo, router, startingPoint);
    }
    
    public double getMoveSpeed(){
        return 9;
    }
}
