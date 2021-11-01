
import java.awt.Graphics;

/**
 * This interface describes an object which can be
 * rendered on screen: compliant objects must probide the
 * contained method
 *
 * @author Calum McConnell
 * @version 0.0.1
 */
public interface Render {
    // This will draw the object on the canvas.
    public void draw(Graphics g);
}