import java.util.function.Function;

/**
 * A standard-issue DoublyLinkedList.
 * This class is moderately optimized for runtime speed: it will access from
 * head or tail depending on which is closest.
 *
 * @author Calum McConnell
 * @version 0.0.1
 */
public class DoublyLinkedList<E>
{
    private Node<E> head;
    private Node<E> tail;
    private int len = 0;
    // indicates whether we are locked; has nothing to do with GC
    private boolean finalized = false;
    
    /**
     * This is an internal method that fetches Node objects.
     * It is used for other list manipulations, and will work optimally.
     * (iterating from head or tail based on the index provided)
     * <br>
     * Does not validate input: prone to throwing null pointer
     * exceptions if input is bad, but is not guarenteed to
     * 
     * @param index the index of the node to fetch
     * @return the node object at that index
     */
    private Node<E> getNodeAtDex(int index){
        Node<E> retval;
        
        if(len/2 - index > 0){
            // go forwards: we're close to the start
            retval = head;
            for(int i = 0; i < index; i++){
                retval = retval.next;
            }
        }
        else{
            // go backwards
            retval = tail;
            for(int i = len-1; i > index; i--){
                retval = retval.prev;
            }
        }
        return retval;
    }
    
    /**
     * Retreives the element at index N.
     * 
     * @param index the index of the element to be retreived
     * @return the Nth element of the list
     */
    public E get(int index){
        return getNodeAtDex(index).data;
    }
    
    /**
     * Removes the element at index n, causing the elements at either
     * side to be adjusted to match it.
     * 
     * @param index the index of the element to be removed
     */
    public void remove(int index){
        // TODO    
    }    
    
    /**
     * Adds an element to the end of the list 
     * @param data the value of the element to add
     */
    public void add(E data){
        add(len-1, data);
    }
    
    /**
     * Adds an element at the provided index, such that a
     * subsequent get(index) will return the value provided.
     * This will move other elements to suit.
     * 
     * @param index the index at which the element is too be placed
     * @param data the data which is within the element
     */
    public void add(int index, E data){
        //TODO
    }
    
    /**
     * Gets the number of elements in this list.
     * 
     * @return the number of elements in this list: 0 if empty
     */
    public int size(){
        return len;
    }
    
    /**
     * This method is a bit ugly: but it's needed to efficently do routings
     * other than "go down the list, stopping at each pickup then each destination"
     * <p>
     * It applies the lambda function to each member of this list one at a time:
     * each result is placed in a new list, which is returned.  For instance, 
     * if this list were to contain ShipmentOrders and the provided function
     * calculated the distance between the current position and the pickup, then
     * the returned value would be each of the doubles that resulted from that
     * calculation
     * @param function The function to apply to each of the List members
     * @return A list of the results, in the same order as this list
     */
    public <G> DoublyLinkedList<G> applyFunctionToList(Function<E,G> function){
        // lets just write this out now
        Node<E> cur = head;
        DoublyLinkedList<G> results = new DoublyLinkedList<G>();
        while(cur != null){
            G result = function.apply(cur.data);
            results.add(result);
        }
        return results;
    }
    
    /**
     * Locks the list in it's current state.  This makes any future
     * add() or remove() operations an exception.  The elements can still
     * be modified, however.
     * <p>
     * There is no unlock() operation. This is intentional.  Its not
     * much of a lock if anyone can open it
     */
    public void lock(){
        finalized = true;
    }
    
    /**
     * States whether the list is currently locked, or if it can still
     * be modified
     * @return true if the list is unable to be modified, false otherwise
     */
    public boolean isLocked(){
        return finalized;
    }
    
    /**
     * States whether or not the list is empty
     * @return true if there are no elements: false otherwise
     */
    public boolean isEmpty(){
        return len==0;
    }
    
    /**
     * Clones the object.  This also clears the isLocked flag, which
     * may be useful.  Note that contained elements are not cloned:
     * this is a shallow cloning operation
     * 
     * @return a shallow clone of this list
     */
    public DoublyLinkedList<E> clone(){
        DoublyLinkedList<E> retval = new DoublyLinkedList<E>();
        // just copy over the references
        retval.head = this.head;
        retval.tail = this.tail;
        retval.len = this.len;
        return retval;
    }
    
    private class Node<E>{
        E data;
        Node<E> prev;
        Node<E> next;
    }
}
