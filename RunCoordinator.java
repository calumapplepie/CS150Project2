import javax.swing.JFrame;


/**
 * Coordinates the runs of a given configuration state
 *
 * @author Calum McConnell
 * @version 0.0.1
 */
public class RunCoordinator
{
    private final DeQueue<Executer> runs = new DeQueue<Executer>();
    private final Configuration runConfig;
    private final JFrame window;
    
    public RunCoordinator(Configuration conf, JFrame win){
        runConfig = conf;
        window = win;
    }
    
    /**
     * Builds and executes each of the runs it was charged with
     * managing.
     */
    public void start(){
        for(int i = 0; i < 10; i++){
            Executer e = new Executer(runConfig, runConfig.initialRandomSeed+i,window);
            runs.add(e);
        }
        
        // Poor mans for-each loop.  I'll probably learn to implement an Iterable
        // interface eventually, but in the mean time, just use this
        runs.applyFunctionToList( (Executer e) -> {e.start(); return null;} );
        
        
        // TODO: generate report
    }
}
