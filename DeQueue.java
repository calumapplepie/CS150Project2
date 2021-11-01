
/**
 * This is a double-ended queue: it allows for elements
 * to be added or removed from both sides.  It therefore
 * can act as a stack or a queue on-demand.
 *
 * @author Calum McConnell
 * @version 0.0.1
 */
public class DeQueue<E> extends DoublyLinkedList<E>
{
    /**
     * Remove and return the first element in the queue
     */
    public E popFront(){
        E retval = get(0);
        remove(0);
        return retval;
    }
    
    /**
     * Return the first element in the queue, without removing it
     */
    public E peekFront(){
        return get(0);
    }
    
    /**
     * Remove and return the last element in the queue
     */
    public E popBack(){
        E retval = peekBack();
        remove(size()-1);
        return retval;
    }
    
    /**
     * Return the last element in the queue, without removing it
     */
    public E peekBack(){
        return get(size()-1);
    }
}
