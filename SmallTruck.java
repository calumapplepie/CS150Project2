
/**
 * Write a description of class SmallTruck here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class SmallTruck extends Truck
{
    SmallTruck(DeQueue<ShipmentOrder> cargo, Router router, Point startingPoint){
        // super call must be first line, so we build the array here
        super(1, cargo, router, startingPoint);
    }
}
