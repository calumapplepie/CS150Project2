import java.util.function.Supplier;
import java.util.Random;

/**
 * Executes a single run of a given state: constructing
 * the requested objects, and then modelling them.
 * 
 * This object is immutable once constructed, though some
 * of its members are mutable.
 *
 * @author Calum McConnell
 * @version 0.0.1
 */
public class Executer
{
    private final DoublyLinkedList<Truck> trucks = new DoublyLinkedList<Truck>();
    private final DoublyLinkedList<Warehouse> warehouses = new DoublyLinkedList<Warehouse>();
    private final Configuration runConfig;
    private final Random randGen;
    
    /**
     * Constructs an executor, which will conduct a run based on the given seed
     * and configuration
     */
    public Executer(Configuration config, long seed){
        randGen = new Random(seed);
        runConfig = config;
    }
    
    /**
     * Called in a loop by start(), this method will run
     * through and schedule entities, increment the clock, and
     * more. 
     * 
     * @return false once it should be stopped
     */
    public boolean execute(){
        //todo
        return false;
    }
    
    /**
     * This method will handle the work of starting the simulation.
     * It will call preprare(), unless prepare() has already been called
     * 
     * This will run execute() repeatedly, until all trucks
     * are finished.  It (may) slow things down a bit, for your
     * viewing pleasure
     */
    public void start(){
        
    }
    
    /**
     * This does the groundwork: generating all the objects needed
     */
    private void prepare(){              
        // this generates our manifests. we define this before we can
        // actually invoke it, 
                
        // now, lets build the warehouses
        // we need to do this before the trucks can be made
        for(int i = 0; i < runConfig.numWarehouses; i++){
            // generate from 1-3, inclusive
            int dockCount = randGen.nextInt(3)+1;
            Warehouse toAdd = new Warehouse(generatePoint(),dockCount);
        }

        
        // now for the small trucks!
        for(int i = 0; i < runConfig.numSmallTrucks; i ++){
            
        }
    }
    
    /**
     * This generates random point objects from the random number generator,
     * reducing boilerplate.  They range from (0,0) to (canvasWidth,canvasHeight)
     */
    private Point generatePoint(){
        return new Point(
                randGen.nextDouble()*runConfig.canvasHeight,
                randGen.nextDouble()*runConfig.canvasWidth);
    }
    
    /**
     * Produces random manifests.  Calling this before the list of warehouses
     * is initialized is a bad idea.  Of course, this method is private, so
     * I know you won't do anything silly like that!
     */
    private DeQueue<ShipmentOrder> generateManifest(){
        if(warehouses.size()!= runConfig.numWarehouses || warehouses.size() < 2){
            throw new Error("I told you not to be silly!  Build those warehouses!");
        }
        
        // i'm tired of writing out that type, lets make the the compiler decide it
        var retval = new DeQueue<ShipmentOrder>();
        
        // alrighty, now lets build the orders!
        for(int i = 0; i < runConfig.numOrdersPerTruck; i++){
            int dex1, dex2;
            
            // Generate indexes until they arent identical
            do{
                // generate an index of warehouse to get, from 1 to the number of warehouses
                dex1 = randGen.nextInt(runConfig.numWarehouses);
                dex2 = randGen.nextInt(runConfig.numWarehouses);
            } while(dex1 == dex2);
            // That might be overkill, since I think my code could tolerate routing
            // out of and back into one warehouse.  But I think it mentions or implies
            // somewhere that the destination and origin should be different.
            
            // now, build the order, and add it to the list
            Warehouse origin = warehouses.get(dex1);
            Warehouse destination = warehouses.get(dex2);
            retval.add(new ShipmentOrder(origin,destination));
        }
        
        return retval;
    }
    
    /**
     * This creates a router for the given manifest, based on 
     */
    private Router createRouter(DeQueue<ShipmentOrder> manifest){
        switch(runConfig.routerID){
            case 0:
                return new NonsenseRouter(manifest);
            default:
                throw new Error("Invalid router ID");
        }
    }
}
