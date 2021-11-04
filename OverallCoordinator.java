/**
 * This class is responsible for creating and executing the various runs
 * and states of the simulation.  It prepares each of the configuration
 * managers, and spins them off to preform each of their runs.
 *
 * @author Calum McConnell
 * @version 0.0.1
 */
public class OverallCoordinator
{
    /**
     * The main method, responsible for triggering the
     * entire rest of this whole mess of a program to run
     */
    public static void main(String[] args){
        DeQueue<RunCoordinator> states = new DeQueue<RunCoordinator>();
        String[] confFiles = {"basic-config.txt","faster-config.txt"};
        for(String i : confFiles){
            Configuration c = Configuration.readConfigFile(i);
            states.add(new RunCoordinator(c));
        }
        
        states.applyFunctionToList( (RunCoordinator t) -> {t.start(); return null;});
    }
}
