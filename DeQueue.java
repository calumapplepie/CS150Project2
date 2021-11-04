
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
    
    public DeQueue<E> clone(){
        // bit of a nasty hack: but a necessary one: otherwise, the user can
        // only get DoublyLinkedList clones, not clones of this.
        
        // we construct a new DeQueue, then append this queue onto it.
        // append necessarily involves conducting a shallow clone:
        // so we do clone this.
        DeQueue<E> retval = new DeQueue<E>();
        retval.append(this);
        
        return retval;
    }
}
