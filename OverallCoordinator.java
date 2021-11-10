import javax.swing.JFrame;

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
        
        // This sets up the window we will be using
        JFrame window = new JFrame();
        window.setSize(8000, 5000);
        window.setTitle("Simulation");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);
        
        for(String i : Configuration.confFiles){
            Configuration c = Configuration.readConfigFile(i);
            states.add(new RunCoordinator(c, window));
        }
        
        states.applyFunctionToList( (RunCoordinator t) -> {t.start(); return null;});
    }
}
