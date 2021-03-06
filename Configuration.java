import java.util.Scanner;
import java.io.File;
import java.io.IOError;

// For the static colors we define
import java.awt.Color;


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
 * <p>
 * Some data fields are also static.  These fields are not read from the config file,
 * but instead are hard-coded.  They describe, for instance, the various colors and
 * sizes of graphics objects.  They are included here to centralize them, and to
 * avoid 'magic constants' being sprinked throughout various classes and the code.
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
    public final Class<? extends Router> routerClass;
    
    public final int canvasWidth;

    public final int canvasHeight;
    public final int initialRandomSeed;
    
    /**
     * The name of this configuration file
     */
    public final String name;
    
    /**
     * The minimum amount of time that should be allowed to elapse between each
     * tick, measured in nanoseconds.  We use nanos instead of milis, because a 1
     * milisecond pause is too long if we want to run through the sim really fast,
     * but 0 milis is too sort for a reasonable experience
     */
    public final int stepGapNanos;
    
    /**
     * The list of valid configuration files, which are to be loaded
     */
    public static final String[] confFiles = {"basic-config.txt","faster-config.txt",
                                                "big-slow-config.txt","big-fast-config.txt"};
    
    /**
     * The length of one side of a square, which will be used to
     * depict Warehouses and Trucks.
     */
    public static final double objectSize = 5;
    
    /**
     * The color of an empty cargo unit in a truck
     */
    public static final Color emptyColor = Color.RED;
    
    /**
     * The color of a filled cargo unit in a truck
     */
    public static final Color filledColor = Color.ORANGE;
    
    /**
     * The color of an empty warehouse
     */
    public static final Color warehouseColorEmpty = Color.BLACK;
    
    /**
     * The color of a not-empty warehouse
     */
    public static final Color warehouseColorBusy = Color.GRAY;

    
    protected Configuration(int small, int medium, int large, int warehouses,
                            int orders, int router,
                            int width, int height, int delay, String name){
        numSmallTrucks = small;
        numMediumTrucks= medium;
        numLargeTrucks = large;
        numWarehouses = warehouses;
        
        numOrdersPerTruck = orders;
        routerID = router;
        routerClass = getRouterFromId(routerID);
        
        canvasWidth = width;
        canvasHeight = height;
        
        stepGapNanos = delay;
        
        // initialRandomSeed is equal to the sum of all other fields
        // this makes it deterministic for a given conf-file, while causing small
        // changes in the conf file to propigate to large changes in the simulation
        
        // the router choice is excluded, so that routers can be compared under identical conditions
        // the delay is also removed, so you can slow it down and watch the same sim again,        
        initialRandomSeed = small + medium + large + warehouses + orders + height + width;
        this.name = name;
    }
    
    public static Configuration readConfigFile(File file){
        
        Configuration retval = null;
        
        // try-with-resources: the resource here is a scanner, constructed
        // with a random file.
        try(Scanner scans = new Scanner(file)){
            // The file format is simply a space-seperated list of the fields
            // contained in the previous section.  This is not a sophisticated format:
            // fancy config files take time to write manually without API classes.
            int numSmallTrucks = scans.nextInt();
            int numMediumTrucks = scans.nextInt();
            int numLargeTrucks = scans.nextInt();
            int numWarehouses = scans.nextInt();
            
            int numOrdersPerTruck = scans.nextInt();
            int routerID = scans.nextInt();
            
            int canvasWidth = scans.nextInt();
            int canvasHeight = scans.nextInt();

            
            int delay = scans.nextInt();
            
            // the accusation that the above lines were produced by running
            //sed 's/public final //g;s/\;/ = scans.nextInt();/'
            // over the instance variable declarations in this class
            // is completly and utterly baseless.
            
            retval = new Configuration(numSmallTrucks, numMediumTrucks, numLargeTrucks, numWarehouses, numOrdersPerTruck, routerID, canvasWidth, canvasHeight, delay, file.getName());
            // only a monster would have used
            //sed 's/.*public final int//g;s/\;/, /'
            // to produce the above line

        }
        // this catch will get any exception relating to an invalid config:
        // including one that is badly formatted or nonexistent.  that's
        // very general, but more precise error handling is out-of-scope
        // for this project
        catch(Exception e){
            System.err.println("Something went wrong reading the config");  
            e.printStackTrace();
            // don't let the machine try to continue with a broken config
            throw new IOError(e);
        }
        
        return retval;
    }
    
    /**
     * This method converts a given router ID into a Class object that
     * represents that router.  Other classes will make use of this object to
     * create the routers that will be used throughout.
     * 
     * This is overly complicated, but I wanted to be able to try out different
     * router designs, and this is one of the simpler ways that can be done
     */
    protected static Class<? extends Router> getRouterFromId(int id){
        switch(id){
            case 0:
                return BadRouter.class;
            case 1:
                return BetterRouter.class;
            default:
                // technically, this isn't a failing assertion: but who's checking?
                // we want this to be a distinct error class
                throw new AssertionError("Invalid router ID");
        }
    }
}
