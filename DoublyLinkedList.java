
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
    private int len;
    
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
     * Removes the (first) element equal to the one provided from the list.
     * For instance, if a list containing [3,4,2,3,4,6,2,3,2,4] has .remove(2)
     * called, then the list will have [3,4,3,4,6,2,3,2,4]
     */
    
    /**
     * Adds an element to the end of the list 
     * @param data the value of the element to add
     */
    public void add(E data){
        //TODO
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
    
    private class Node<E>{
        E data;
        Node<E> prev;
        Node<E> next;
    }
}
