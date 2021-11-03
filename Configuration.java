
/**
 * Manages the configuration: reading it in from the file, and then
 * presenting it in a usable format.
 * <p>
 * Most configuration systems are key-value stores.  They are each based on 
 * a Map data structure.  Unfortunately, we haven't learned about Maps yet,
 * and implementing my own for no good reason would be a bit silly.
 * So, we instead have this: A key-value system that is more like a named tuple.
 * (the julia term for this)
 * <p>
 * The data fields are public and final.  This doesn't break encapsulation:
 * as they are final and primitive, the user cannot in any way change them.
 * That also makes this object immutable once created.  
 * The constructor is protected, to allow for testing while still 'advising'
 * against the manual creation of Configuration objects.
 * 
 * @author Calum McConnell
 * @version 0.0.1
 */
public class Configuration
{
    public final int numSmallTrucks;
    public final int numMediumTrucks;
    public final int numLargeTrucks;
    public final int numWarehouses;
    
    public final int numOrdersPerTruck;
    public final int routerID;
    
    public final int canvasHeight;
    public final int canvasWidth;
    
    protected Configuration(int small, int medium, int large, int warehouses,
                            int orders, boolean balance, int router,
                            int height, int width){
        numSmallTrucks = small;
        numMediumTrucks= medium;
        numLargeTrucks = large;
        numWarehouses = warehouses;
        
        numOrdersPerTruck = orders;
        routerID = router;
        
        canvasHeight = height;
        canvasWidth = width;
    }
    
    public static DeQueue<Configuration> readConfigFile(String filename){
        return new DeQueue<Configuration>();
    }
}
