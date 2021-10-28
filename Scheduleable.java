
/**
 * This is my version of the Schedule interface described in the docs.
 * It is somewhat different, because I have naming conventions I bend
 * it too, and because I don't like a design based on allowing
 * multiple different objects to write to a file: I like for that
 * to be centralized.
 *
 * @author Calum McConnell
 * @version 2
 */
public interface Scheduleable {
    /**
     * Called each hour, allowing the object to perform an action.
     */ 
    public void action();
    /**
     * Will store the object’s current information into a log file.
     */ 
    public String status();
}
