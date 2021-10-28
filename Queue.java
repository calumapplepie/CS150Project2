
/**
 * Write a description of class Queue here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Queue<E> extends DoublyLinkedList<E>
{
    public E popFront(){
        return(get(0));
    }
}
