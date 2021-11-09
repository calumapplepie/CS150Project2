import javax.swing.JComponent;
import java.awt.Graphics;
import java.awt.Graphics2D;


/**
 * Write a description of class SimulationImage here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class SimulationImage extends JComponent
{
    private final DoublyLinkedList<Render> objects = new DoublyLinkedList<Render>();
    
    public void add(Render obj){
        objects.add(obj);
    }
    
    
    @Override
    public void paintComponent(Graphics g){
        objects.resetFakeQueue();
        for(int i = 0; i < objects.size(); i++){
            objects.fakePop().draw( (Graphics2D) g);
        }
    }
    
}
