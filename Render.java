
import java.awt.Graphics2D;

/**
 * This interface describes an object which can be
 * rendered on screen: compliant objects must provide the
 * contained method
 *
 * @author Calum McConnell
 * @version 0.0.1
 */
public interface Render {
    /**
     * This will draw the object on the canvas.  It expects
     * a Graphics2D object that describes the screen being rendered to.
     * 
     * @param g the graphics object that will be drawn on.  Since it is always
     * casted to a Graphics2D, we just make the type requirements explicit and
     * use that as the parameter.
     */
    public void draw(Graphics2D g);
}