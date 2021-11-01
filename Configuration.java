
/**
 * Manages the configuration: reading it in from the file, and then
 * presenting it in a usable format.
 * <p>
 * Most configuration systems are key-value stores.  They are each based on 
 * a Map data structure.  Unfortunately, we haven't learned about Maps yet,
 * and implementing my own for no good reason would be a bit silly.
 * So, we instead have this: A key-value system that isn't.
 *
 * @author Calum McConnell
 * @version 0.0.1
 */
public class Configuration
{
    /**
     * Private constructor ensures that configuration objects are
     * only produced through reading of a file.  If, in future, we wish to
     * support a person writing their own configuration and then saving it
     * to disk from within the program, this API might need to be changed.
     */
    private Configuration(){
        
    }
    
    public static DeQueue<Configuration> readConfigFile(String filename){
        return new DeQueue<Configuration>();
    }
}
