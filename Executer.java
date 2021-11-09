import java.util.function.Supplier;
import java.util.Random;

import javax.swing.JFrame;

import java.lang.reflect.Constructor;

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
    private final JFrame window;
    private int ticks = 0;
    
    /**
     * Constructs an executor, which will conduct a run based on the given seed
     * and configuration
     */
    public Executer(Configuration config, long seed, JFrame graphics){
        randGen = new Random(seed);
        runConfig = config;
        window = graphics;
    }
    
    /**
     * Called in a loop by start(), this method will run
     * through and schedule entities, increment the clock, and
     * more. 
     * 
     * @return false once it should be stopped
     */
    public boolean execute(){
        // Increase the central clock by one hour
        ticks++;
        
        // First,  iterate through the warehouse and truck lists, executing each one, then drawing it.
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
        prepareSimulation();
        prepareGraphics();
        
        // We want to run untill execute() returns false.  But to make the graphics understandable,
        // we don't want tu run too often.  This starts one tick every timeDelta (class variable) miliseconds,
        // counting from when the last tick started.  Thus, most noise in the execution time of execute()
        // is insulated away.
        long timeStart = System.currentTimeMillis();
        while(execute()){
            long timeToSleep = Configuration.stepGap -(System.currentTimeMillis() - timeStart);
            timeStart = System.currentTimeMillis();
            // now sleep, but not for a negative duration
            try{
                Thread.sleep(Math.max(timeToSleep, 0));
            }
            catch(Exception e){
                System.err.println("We were interruped, apparently!?!?");
                throw new Error("Interrupted, despite a lack of multithreading", e);
            }
        }
    }
    
    /**
     * This does the groundwork: generating all the objects needed
     * for the simulation to work
     */
    private void prepareSimulation(){              
        // this generates our manifests. we define this before we can
        // actually invoke it, 
                
        // now, lets build the warehouses
        // we need to do this before the trucks can be made
        for(int i = 0; i < runConfig.numWarehouses; i++){
            // generate from 1-3, inclusive
            int dockCount = randGen.nextInt(3)+1;
            Warehouse toAdd = new Warehouse(generatePoint(),dockCount);
            warehouses.add(toAdd);
        }

        
        // now for the small trucks!
        for(int i = 0; i < runConfig.numSmallTrucks; i ++){
            Truck truck = new SmallTruck(generateManifest(), runConfig.routerClass,generatePoint());
            trucks.add(truck);
        }
        
        // and the mediums...
        for(int i = 0; i < runConfig.numMediumTrucks; i++){
            Truck truck = new MediumTruck(generateManifest(), runConfig.routerClass,generatePoint());
            trucks.add(truck);
        }
        
        // and the larges
        for(int i = 0; i < runConfig.numLargeTrucks; i++){
            Truck truck = new LargeTruck(generateManifest(), runConfig.routerClass,generatePoint());
            trucks.add(truck);
        }
    }
    
    /**
     * This handles preparing for the graphics rendering
     */
    private void prepareGraphics(){
            
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
}
