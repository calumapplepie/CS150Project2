import javax.swing.JComponent;
import java.awt.Graphics;
import java.awt.Graphics2D;


/**
 * Represents the graphical representation of the simulation:
 * this component paints the images of the sim.
 *
 * @author Calum McConnell
 * @version 0.0.1
 */
public class SimulationImage extends JComponent
{
    private final DoublyLinkedList<Render> objects = new DoublyLinkedList<Render>();
    private final double scaleFactor;
    
    SimulationImage(double scale){
        scaleFactor = scale;
    }
    
    public void add(Render obj){
        objects.add(obj);
    }
    
    
    @Override
    public void paintComponent(Graphics g){
        Graphics2D graph = (Graphics2D) g;
        // re-scale
        graph.scale(scaleFactor,scaleFactor);
        
        objects.resetFakeQueue();
        for(int i = 0; i < objects.size(); i++){
            objects.fakePop().draw(graph);
        }
        
        //de-scale
        graph.scale(1/scaleFactor,1/scaleFactor);

    }
    
}
