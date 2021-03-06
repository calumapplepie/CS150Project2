import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

import javax.swing.JComponent;

/**
 * A single warehouse, which "contains" goods to be shipped.
 *
 * @author Calum McConnell
 * @version 0.0.1
 */
public class Warehouse implements Schedule, Render
{
    public final Point location;
    public final int docks;
    private final DeQueue<Truck> enter = new DeQueue<Truck>();
    // This queue holds a truck for a single round, to prevent one from
    // entering and leaving on the same tick
    private final DeQueue<Truck> exits = new DeQueue<Truck>();
    
    /**
     * Builds a warehouse
     * @param p the location to build the warehouse at
     * @param docks the number of loading docks that this warehouse has
     */
    public Warehouse(Point p, int docks){
        location = p;
        this.docks = docks;
    }
    
    /**
     * Adds the given truck to the entrance queue
     */
    public void joinQueue(Truck t){
        enter.add(t);
    }
    
    /**
     * Steps through: a number of trucks equal to the loading docks
     * are popped out of the queue, and given the appropriate cargo,
     * before being placed into an exit queue.  
     */
    public void action(){
        // empty the exiting queue
        while(exits.size() != 0){
            Truck target = exits.popFront();
            target.loadingComplete();
        }
        // pull a number of trucks equal to docks out of the entrance queue,
        // placing them into the exiting queue
        for(int i = 0; i < docks && enter.size() > 0; i++){
            exits.add(enter.popFront());
        }
    }
    
    /**
     * Prepares a log string indicating how many trucks are in the entry
     * and exit queues.
     */
    public String status(){
        return enter.size() + " Trucks entering, " + exits.size() + " Trucks leaving";
    }
    
    public void draw(Graphics2D g){
        double y = location.yPos - Configuration.objectSize/2;
        double x = location.xPos - Configuration.objectSize/2;
        Rectangle2D rect = new Rectangle2D.Double(x,y,Configuration.objectSize*2,Configuration.objectSize*2);
        if(enter.size() + exits.size() > 0){
            g.setColor(Configuration.warehouseColorBusy);
        }
        else{
            g.setColor(Configuration.warehouseColorEmpty);
        }
        g.fill(rect);

    }

}
