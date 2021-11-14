import java.io.File;
import java.io.FileWriter;
import java.io.IOError;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * This tests the loading of a config file, verifying that fields wind up where they should.
 *
 * @author  (your name)
 * @version (a version number or a date)
 */
public class ConfigurationTest
{
    File configFileOne;
    File configFileTwo;
    File invalidConfig;
    File invalidRouter;
    
    /**
     * Sets up the test fixture, creating a series of config files
     */
    @BeforeEach
    public void setUp()
    {
        // Creates and fills the tempfiles
        try{
            // We create some tempfiles, which are (on my machine) placed in /tmp/ .
            // that means they will be deleted on system restart, regardless of what
            // else we do
            configFileOne = File.createTempFile("javatest",null);
            configFileTwo = File.createTempFile("javatest",null);
            invalidConfig = File.createTempFile("javatest",null);
            invalidRouter = File.createTempFile("javatest",null);
            
            // to be sure, tell the JVM to delete them on exit: unlike linux, 
            // windows lets tempfiles accumulate, until you use a cleaner app.
            configFileOne.deleteOnExit();
            configFileTwo.deleteOnExit();
            invalidConfig.deleteOnExit();
            invalidRouter.deleteOnExit();
            
            var f = new FileWriter(configFileOne);
            // the random "0" is for the router id
            f.write("1 2 3 4 5 0 7 8 9");
            f.close();
            
            // now, for the next file
            f = new FileWriter(configFileTwo);
            f.write("9 8 7 6 5 1 3 2 0");
            f.close();
            
            // and next, with a nonsense file
            f = new FileWriter(invalidConfig);
            f.write("boo");
            f.close();
            
            // and one more, with a almost-good file (except for the nonexistent router
            f = new FileWriter(invalidRouter);
            f.write("1 2 3 4 5 6 7 8 9");
        }
        catch(Exception e){
            e.printStackTrace();
            throw new Error("failed to init config files");
        }
    }
    
    @Test
    public void filesReadCorrectly(){
        Configuration config = Configuration.readConfigFile(configFileOne);
        
        // I absolutely did not use sed scripting to produce those lines.
        // just like those other lines in the Configuration class that I wrote by hand
        assertEquals(config.numSmallTrucks, 1);
        assertEquals(config.numMediumTrucks, 2);
        assertEquals(config.numLargeTrucks, 3);
        assertEquals(config.numWarehouses, 4);
            
        assertEquals(config.numOrdersPerTruck, 5);
        assertEquals(config.routerID, 0);
        assertEquals(config.routerClass, BadRouter.class);
            
        assertEquals(config.canvasWidth, 7);
        
        assertEquals(config.canvasHeight, 8);
        assertEquals(config.stepGapNanos, 9);
        
        // also check the random seed explicitly
        assertEquals(config.initialRandomSeed, 1+2+3+4+5+7+8);
        
        // now, for the other valid config!
        config = Configuration.readConfigFile(configFileTwo);
        
        // more code produced without any sort of so-called "sed script"
        assertEquals(config.numSmallTrucks, 9);
        assertEquals(config.numMediumTrucks, 8);
        assertEquals(config.numLargeTrucks, 7);
        assertEquals(config.numWarehouses, 6);
            
        assertEquals(config.numOrdersPerTruck, 5);
        assertEquals(config.routerID, 1);
        assertEquals(config.routerClass, BetterRouter.class);
            
        assertEquals(config.canvasWidth, 3);
        
        assertEquals(config.canvasHeight, 2);
        assertEquals(config.stepGapNanos, 0);
        
        assertEquals(config.initialRandomSeed, 9+8+7+6+5+3+2);
        
        // now, for the invalid configurations!
        assertThrows(IOError.class, ()->Configuration.readConfigFile(invalidConfig));
        // even though we throw an AssertionError in this case, it gets caught and wrapped in an
        // IO error
        assertThrows(Error.class, ()-> Configuration.readConfigFile(invalidRouter));
        
        // lets be sure that an assertion error is, in fact, thrown in those cases.
        assertThrows(AssertionError.class, ()-> Configuration.getRouterFromId(2));

    }

    /**
     * Tears down the test fixture, deleting those files
     * If this isn't invoked (say, due to someone canceling the tests),
     * the files will still get removed eventually, when the JVM exits
     */
    @AfterEach
    public void tearDown()
    {
        // the delete() method of the File class doesn't throw exceptions, surprisingly
        // since we don't really care if deletion is successful (if it fails,
        // they'll be removed later by other things
        configFileOne.delete();
        configFileTwo.delete();
        invalidConfig.delete();
        invalidRouter.delete();
    }
}
