import javax.swing.JFrame;
import java.io.FileWriter;
import java.io.File;

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
            Configuration c = Configuration.readConfigFile(new File(i));
            states.add(new RunCoordinator(c, window));
        }
        
        // the type of the next line is a generic of a generic, so lets make the compiler picture that
        // rather than wrtie it out ourselves.
        var rawStatuses = states.applyFunctionToList( (RunCoordinator t) ->t.start());
        
        // now, lets unpack that mess
        DoublyLinkedList<String> statuses = new DoublyLinkedList<String>();
        rawStatuses.applyFunctionToList( (DoublyLinkedList<String> s) -> { statuses.append(s); return null;});
        
        try(FileWriter logger = new FileWriter("summary-log.csv")){
            for(int i = 0; i < statuses.size(); i++){
                logger.write(statuses.fakePop());
            }
        }
        catch(Exception e){
            throw new Error("Failed to log status summary", e);
        }
        // close the program.
        window.dispose();
    }
}
